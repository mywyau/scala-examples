package zio

import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}
import org.openqa.selenium.{By, WebDriverException}
import zio.selenium.WebDriver

object FindElement extends ZIOAppDefault {

  val options = new ChromeOptions()
  options.addArguments("--remote-allow-origins=*")
  val webdriver = new ChromeDriver(options)

  val app: ZIO[WebDriver, Throwable, Unit] =
    for {
      webpage <- WebDriver.get("https://kibana.tools.production.tax.service.gov.uk/login")
      h1Text <- WebDriver.findElement(By.xpath("/html/body/div[1]/div[3]/div[2]/div/header/div/h1")).map(_.getText)
      _ <- Console.printLine(s"H1: $h1Text")
      username <- WebDriver.findElement(By.xpath("/html/body/div[1]/div[3]/div[2]/div/div/div/div/div/form/div[1]/div[2]/div/div/input"))
      _ <- username.sendKeys("michael.yau")
      _ <- Console.printLine(s"Username: michael.yau")
      password <- WebDriver.findElement(By.xpath("/html/body/div[1]/div[3]/div[2]/div/div/div/div/div/form/div[2]/div[2]/div/div/input"))
      _ <- password.sendKeys("Zv10f9ghe25995!")
      _ <- Console.printLine(s"Password: Zv10f9ghe25995!")
      loginButton <- WebDriver.findElement(
        By.cssSelector("/html/body/div[1]/div[3]/div[2]/div/div/div/div/div/form/div[4]/div/button/span")
      )
      _ <- loginButton.click
    } yield ()

  val layer: Layer[WebDriverException, WebDriver] = WebDriver.layer(webdriver)

  override def run = app.provide(layer)
}