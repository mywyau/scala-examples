import org.joda.time.DateTimeZone

import java.time.{LocalDateTime => JavaLocalDateTime}
import java.time.format.DateTimeFormatter
import scala.util.chaining.scalaUtilChainingOps
import org.joda.time.{DateTimeZone, LocalDateTime => JodaLocalDateTime }

object DateTimeTest extends App {

  val time : JavaLocalDateTime = JavaLocalDateTime.now()
//  val time2 : JodaLocalDateTime = JodaLocalDateTime.now()

  val time2: JodaLocalDateTime  = {
    val zone = DateTimeZone.forID("Europe/London")
    JodaLocalDateTime.now(zone)
  }
//
//  val time3 = JavaLocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
//  val time4 = JodaLocalDateTime.now().toString("yyyyMMddHHmmss")

  time.tap(println)
  time2.tap(println)
//  time2.tap(println)
//  time3.tap(println)
//  time4.tap(println)
}
