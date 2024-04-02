package ConcurrentATs

import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}
import org.openqa.selenium.{By, WebDriver}

import scala.annotation.tailrec

//object RulingFrontendProd extends App {
//
//
//}

object RulingFrontendProd extends App {

  System.setProperty("webdriver.chrome.driver", "/usr/local/Caskroom/chromedriver/121.0.6167.85/chromedriver-mac-x64/chromedriver")

  // Optional: Set Chrome options (e.g., to run headless)

  val options = new ChromeOptions()
  options.addArguments("--remote-allow-origins=*")

  // options.addArguments("--headless") // Uncomment this line to run headless

  // Create a new instance of ChromeDriver
  val webDriver: WebDriver = new ChromeDriver(options)

  val productionService = "https://www.tax.service.gov.uk/search-for-advance-tariff-rulings/search?page=1"

  def rulingCaseRefSelector(i: Int) = s"#search_results-list-$i > h3"

  def findRulingCaseRef(i: Int): String =
    webDriver.findElement(By.cssSelector(rulingCaseRefSelector(i))).getText

  def getAllSearchResultsForPage: Seq[String] = {

    @tailrec
    def loop(start: Int, finish: Int, acc: Seq[String]): Seq[String] =
      start match {
        case n if n == finish => acc
        case n =>
          loop(n + 1, finish, acc :+ findRulingCaseRef(n))
      }

    val rulingReferences = loop(1, 25, Seq())
    println(rulingReferences)
    rulingReferences
  }

  val nextLink = "#search-pagination_bottom-page_next > a"

  val nextLinkPresentOnPage: Boolean = webDriver.findElement(By.cssSelector(nextLink)).isDisplayed

  def clickNextLink(): Unit = webDriver.findElement(By.cssSelector(nextLink)).click()

  def determinePageIsCorrect(pageNo: Int): Boolean =
    webDriver.getCurrentUrl == s"https://www.tax.service.gov.uk/search-for-advance-tariff-rulings/search?page=$pageNo"

  def loopThroughAllPages: Seq[(String, Int)] = {

    @tailrec
    def loop(pageNumber: Int, nextLinkPresent: Boolean, acc: Seq[(String, Int)]): Seq[(String, Int)] =
      nextLinkPresent match {
        case true =>
          if (determinePageIsCorrect(pageNumber)) {
            println(
              webDriver.getCurrentUrl.toString
                .replace("https://www.tax.service.gov.uk/search-for-advance-tariff-rulings/search?", "")
            )
            val allCasesOnPage =
              getAllSearchResultsForPage.zipWithIndex.map { case (str, i) =>
                (str, pageNumber)
              }
            clickNextLink()
            loop(pageNumber + 1, nextLinkPresent, acc ++ allCasesOnPage)
          } else {
            throw new RuntimeException(s"Failed to match url for pageNo: $pageNumber")
          }
        case false => acc

      }

    val rulingReferencesWithPageNo = loop(1, nextLinkPresentOnPage, Seq())
    println(rulingReferencesWithPageNo)
    rulingReferencesWithPageNo
  }

  webDriver.get(productionService)
  val findDuplicates: Map[String, Int] =
    loopThroughAllPages.groupBy(identity).collect { case (x, ys) if ys.lengthCompare(1) > 0 => x }
  println(findDuplicates)


  // Close the browser
  webDriver.quit()
}