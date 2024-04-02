package ConcurrentATs

import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}
import org.openqa.selenium.{By, WebDriver}

import scala.annotation.tailrec

object RulingFrontendProd extends App {

  System.setProperty("webdriver.chrome.driver", "/usr/local/Caskroom/chromedriver/123.0.6312.86/chromedriver-mac-x64/chromedriver")

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

  def numberOfSearchesOnPage: Int = webDriver.findElements(By.cssSelector("#search_results-list > li")).size()

  def getAllSearchResultsForPage: Seq[String] = {

    @tailrec
    def loop(start: Int, finish: Int, acc: Seq[String]): Seq[String] =

      start match {
        case n if n == finish => acc
        case n =>
          loop(n + 1, finish, acc :+ findRulingCaseRef(n))
      }

    val rulingReferences = loop(0, finish = numberOfSearchesOnPage, acc = Seq())
    println(rulingReferences)
    rulingReferences
  }

  val nextLink = "#search-pagination_bottom-page_next > a"

  def clickNextLink(): Unit = webDriver.findElement(By.cssSelector(nextLink)).click()

  def determinePageIsCorrect(pageNo: Int): Boolean =
    webDriver.getCurrentUrl == s"https://www.tax.service.gov.uk/search-for-advance-tariff-rulings/search?page=$pageNo"

  def loopThroughAllPages: Seq[(String, Int)] = {

    @tailrec
    def loop(pageNumber: Int, acc: Seq[(String, Int)]): Seq[(String, Int)] =
      if (determinePageIsCorrect(pageNumber)) {
        println(
          "\n" + webDriver.getCurrentUrl.toString
            .replace("https://www.tax.service.gov.uk/search-for-advance-tariff-rulings/search?", "")
        )

        val allCasesOnPage = {
          getAllSearchResultsForPage.zipWithIndex.map { case (str, i) =>
            (str, pageNumber)
          }
        }

        if (numberOfSearchesOnPage == 25) {
          clickNextLink()
        }

        loop(pageNumber + 1, acc ++ allCasesOnPage)
      } else {
        acc
      }

    val rulingReferencesWithPageNo = loop(1, Seq())
    val duplicates: Seq[String] = rulingReferencesWithPageNo.map(_._1).diff(rulingReferencesWithPageNo.map(_._1).distinct).distinct

    val duplicatesWithPageNumber: Seq[(String, Int)] = rulingReferencesWithPageNo.collect { case tuple: (String, Int) if duplicates.contains(tuple._1) =>
      tuple
    }

    println("\nduplicatesWithPageNumber " + duplicatesWithPageNumber)

    //    println(rulingReferencesWithPageNo)  // print all cases with page number
    rulingReferencesWithPageNo
  }

  webDriver.get(productionService)

  println("\nnumberOfSearchesOnPage: " + numberOfSearchesOnPage)

  loopThroughAllPages

  // Close the browser
  webDriver.quit()
}