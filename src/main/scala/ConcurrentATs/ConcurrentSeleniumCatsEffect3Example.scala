package ConcurrentATs

import cats.effect._
import cats.effect.implicits.{genSpawnOps, genTemporalOps_}
import fs2.Stream
import fs2.concurrent.SignallingRef
import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}
import org.openqa.selenium.{By, WebDriver}
import scala.util.chaining._

import scala.concurrent.duration.DurationInt

object ConcurrentSeleniumCatsEffect3Example extends IOApp {

  // Max number of concurrent instances to run at any one time
  val maxConcurrentInstances: Int = 5
  val maxNumberOfRepetitons: Int = 10

  // Function to create a ChromeDriver instance as a Resource, handles safe opening of a resource and graceful shutdown
  def createWebDriverResource[F[_] : Sync]: Resource[F, WebDriver] =
    Resource.make(Sync[F].delay {
      System.setProperty("webdriver.chrome.driver", "/usr/local/Caskroom/chromedriver/121.0.6167.85/chromedriver-mac-x64/chromedriver")
      val options = new ChromeOptions()
      options.addArguments("--remote-allow-origins=*")
      new ChromeDriver(options)
    })(driver => Sync[F].delay(driver.quit()))

  // Function to perform Selenium actions with a given WebDriver instance
  def runSeleniumUserJourneyProgram[F[_] : Sync](driver: WebDriver): F[Unit] =
    Sync[F].delay {

      driver.get("https://www.google.com")
      val rejectCookiesButton = driver.findElement(By.cssSelector("#W0wltc > div"))
      rejectCookiesButton.click()

      // mimic a user journey via iterating steps - dirty but for the sake of not having to write out tons of steps
      val i = 0
      for (n <- i until maxNumberOfRepetitons by 1) {
        driver.get("https://www.google.com")
        val searchBox = driver.findElement(By.name("q"))
        searchBox.sendKeys("Selenium WebDriver")
        searchBox.submit()
        driver.get("https://www.youtube.com")

        // Print the browser handle id and title of the current page
        driver.getWindowHandle.tap(println)
        println(s"Page title: ${driver.getTitle}, loop: $n \n")
      }
    }

  //   Concurrently run the Selenium program with multiple WebDriver instances
  def runConcurrentSeleniumPrograms[F[_]]()(implicit AS: Async[F]): Stream[F, Unit] = {

    def createWebdriverInstances(numberOfInstancesToCreate: Long) = {
      Stream.resource(createWebDriverResource[F]).repeatN(numberOfInstancesToCreate)
    }

    val createSwitch: Stream[F, SignallingRef[F, Boolean]] = Stream.eval(SignallingRef[F, Boolean](false))

    for {
      switch: SignallingRef[F, Boolean] <- createSwitch
      timedSwitchOffAfter300s <- Stream.eval(switch.set(true).delayBy(300.seconds).start)
      _ <- createWebdriverInstances(maxConcurrentInstances)
        .interruptWhen(switch)
        .parEvalMapUnordered(maxConcurrentInstances) { driver =>
          runSeleniumUserJourneyProgram(driver)
        }
    } yield {
      ()
    }
  }

  // Implement the run method for IOApp (our program)
  override def run(args: List[String]): IO[ExitCode] =
    runConcurrentSeleniumPrograms[IO]
      .compile
      .drain
      .as(ExitCode.Success)
      .guarantee(IO.delay(println("All instances completed")))
}