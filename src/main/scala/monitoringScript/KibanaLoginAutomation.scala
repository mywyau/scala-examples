package monitoringScript

import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}
import org.openqa.selenium.{By, WebDriver}
import java.nio.file.{Paths, Files}
import java.util.UUID

object KibanaLoginAutomation extends App {
  // Set the path to your ChromeDriver executable
  System.setProperty("webdriver.chrome.driver", "/usr/local/Caskroom/chromedriver/121.0.6167.85/chromedriver-mac-x64/chromedriver")

  // Create a ChromeOptions object to configure the ChromeDriver
  val chromeOptions = new ChromeOptions()
  chromeOptions.addArguments("--remote-allow-origins=*")
  // You can add additional options if needed

  val downloadDir = Paths.get("src/main/resources/downloads")
//  chromeOptions.("download.default_directory", downloadDir.toAbsolutePath.toString)


  // Create a WebDriver instance using ChromeDriver and the configured options
  val driver: WebDriver = new ChromeDriver(chromeOptions)

  // Navigate to Kibana login page
  driver.get("https://kibana.tools.production.tax.service.gov.uk/login?next=%2Fapp%2Fdashboards#/view/01436b50-197f-11ed-ab4e-4fbd37e30ecc?_g=(filters:!(),refreshInterval:(pause:!t,value:0),time:(from:'2024-02-22T09:00:00.000Z',to:'2024-02-22T17:00:00.000Z'))")

  Thread.sleep(3000)
  // Find the username and password input fields and enter your credentials
  driver.findElement(By.name("username")).sendKeys("michael.yau")
  driver.findElement(By.name("password")).sendKeys("Zv10f9ghe25995!")

  // Find and click the login button
//  driver.findElement(By.xpath("//button[contains(text(),'Log in')]")).click()
  driver.findElement(By.xpath("/html/body/div[1]/div[3]/div[2]/div/div/div/div/div/form/div[4]/div/button")).click()
  Thread.sleep(1000)
  driver.findElement(By.cssSelector("#panel-1 > div > figcaption > div > div > button")).click()  // more button
  Thread.sleep(1000)
  driver.findElement(By.cssSelector("body > div:nth-child(5) > div:nth-child(2) > div > div:nth-child(3) > div > div > div:nth-child(2) > button:nth-child(4) > span > span")).click()
  Thread.sleep(1000)
  driver.findElement(By.cssSelector("body > div:nth-child(5) > div:nth-child(2) > div > div:nth-child(3) > div > div > div > button:nth-child(1) > span > span")).click()

//  driver.findElement(By.cssSelector("#panel-3 > div > figcaption > div > div > button")).click()


  // Optional: You may want to add some waiting time to ensure the login is complete
  Thread.sleep(2000)

  // Verify that you are logged in by checking for a specific element on the dashboard
  val isLoggedIn = driver.findElement(By.id("#kibana-body > div.header__topBanner > div > div > div > div > p")).getText == "production"
  if (isLoggedIn) {
    println("Login successful!")
  } else {
    println("Login failed.")
  }

  // Close the browser window
  driver.quit()
}
