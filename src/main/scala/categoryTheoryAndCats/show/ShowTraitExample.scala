package scala.learnCatsAndStuff.show

import java.time.LocalDate

import cats.Show
import cats.syntax.show._
import cats.instances.all._

trait ShowTraitExample {

  /*
  Importing Interface Syntax
    We can make Show easier to use by importing the interface syntax from
    cats.syntax.show. This adds an extension method called show to any type
    for which we have an instance of Show in scope:
*/

  // for show

  val shownInt: String = 123.show // this still needs an implicit which can be provided by   'import cats.instances.all._' the syntax is different though to the other example

  val shownString: String = "abc".show

  /*
    All the imports for your importing needs

    • import cats._ imports all of Cats’ type classes in one go;
    • import cats.instances.all._ imports all of the type class instancesfor the standard library in one go;
    • import cats.syntax.all._ imports all of the syntax in one go;
    • import cats.implicits._ imports all of the standard type class instances and all of the syntax in one go.

    general imports until you need more

    import cats._
    import cats.implicits._

  */

  /* Making a custom show for java.util.LocalDate */

  // standard way of defining a Show for LocalDate

  implicit val localDateShow: Show[LocalDate] = {
    new Show[LocalDate] {
      def show(date: LocalDate): String = s"dateShow1: $date"
    }
  }

  // Cats has a method to define Show[A] from functions  f: A => String

  // shorthand definition for Show[LocalDate] by using a function from cats

  //  implicit val dateShow2: Show[LocalDate] = {
  //    Show.show(date => s"dateShow2: ${date}ms since epoch")
  //  }
  //
  //  implicit val dateShow3: Show[LocalDate] = {
  //    Show.fromToString
  //  }
}

trait Show2 {

  // Cats has a method to define Show[A] from functions  f: A => String

  // shorthand definition for Show[LocalDate] by using a function from cats

  implicit val dateShow2: Show[LocalDate] = {
    Show.show(date => s"dateShow2: $date mikey")
  }

  //  implicit val dateShow3: Show[LocalDate] = {
  //    Show.fromToString
  //  }
}

trait Show3 {

  // Cats has a method to define Show[A] from the default .toString() method on stuff

  implicit val dateShow3: Show[LocalDate] = {
    Show.fromToString
  }
}

object Runner extends App with ShowTraitExample {

  println(LocalDate.of(2020, 1, 1).show)  // dateShow1: 2020-01-01
}

object Runner2 extends App with Show2 {

  println(LocalDate.of(2020, 1, 1).show) // dateShow2: 2020-01-01 mikey
}

object Runner3 extends App with Show3 {

  println(LocalDate.of(2020, 1, 1).show)  // returns default .toString()  2020-01-01
}
