import cats.effect._
import cats.effect.implicits.{genSpawnOps, genTemporalOps_}
import fs2.Stream
import fs2.concurrent.SignallingRef
import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}
import org.openqa.selenium.{By, WebDriver}

import scala.concurrent.duration.DurationInt

object ConcurrentSeleniumCatsEffect3Example extends IOApp {

  // Function to create a ChromeDriver instance as a Resource
  def createWebDriverResource[F[_] : Sync]: Resource[F, WebDriver] =
    Resource.make(Sync[F].delay {
      System.setProperty("webdriver.chrome.driver", "/usr/local/Caskroom/chromedriver/121.0.6167.85/chromedriver-mac-x64/chromedriver")
      val options = new ChromeOptions()
      options.addArguments("--remote-allow-origins=*")
      new ChromeDriver(options)
    })(driver => Sync[F].delay(driver.quit()))

  // Function to perform Selenium actions with a WebDriver instance
  def runSeleniumProgram[F[_] : Sync](driver: WebDriver): F[Unit] =
    Sync[F].delay {

      driver.get("https://www.google.com")
      val rejectCookiesButton = driver.findElement(By.cssSelector("#W0wltc > div"))
      rejectCookiesButton.click()

      val i = 0
      for (n <- i until 50 by 1) {
        driver.get("https://www.google.com")
        val searchBox = driver.findElement(By.name("q"))
        searchBox.sendKeys("Selenium WebDriver")
        searchBox.submit()
        driver.get("https://www.youtube.com")
        // Print the title of the current page
        println(s"Page title: ${driver.getTitle} loop: $n")
      }
    }

  //   Concurrently run the Selenium program with multiple WebDriver instances
  def runConcurrentSeleniumPrograms[F[_]]()(implicit AS: Async[F]): Stream[F, Unit] = {

    def program() = {
      Stream.resource(createWebDriverResource[F]).repeat
    }

    val createSwitch = Stream.eval(SignallingRef[F, Boolean](false))

    for {
      switch <- createSwitch
      _ <- Stream.eval(switch.set(true).delayBy(300.seconds).start)
      _ <- program()
        .interruptWhen(switch)
        .parEvalMapUnordered(maxConcurrentInstances) { driver =>
          runSeleniumProgram(driver)
        }
    } yield {
      ()
      println(System.currentTimeMillis())
    }
  }

  // Number of concurrent instances to run
  val maxConcurrentInstances: Int = 5

  // Implement the run method for IOApp
  override def run(args: List[String]): IO[ExitCode] =
    runConcurrentSeleniumPrograms[IO]
      .compile
      .drain
      .as(ExitCode.Success)
      .guarantee(IO.delay(println("All instances completed")))

  // Main method to execute the program
  //  def main: IO[Unit] = run(Nil)
}