package scala.learnCatsAndStuff.show

import cats.Show
import cats.instances.all._   // imports instances for all types supported out of the box by cats

class ShowExample {

/*
  The cats.instances package provides default instances for a wide variety of
  types. We can import these as shown in the table below. Each import provides
  instances of all Cats’ type classes for a specific parameter type:

    • cats.instances.int provides instances for Int
    • cats.instances.string provides instances for String
    • cats.instances.list provides instances for List
    • cats.instances.option provides instances for Option
    • cats.instances.all provides all instances that are shipped out of the box with Cats

  See the cats.instances package for a complete list of available imports

*/

  val showInt = Show.apply[Int]   //without an implicit for Int this would blow up
  val showString: Show[String] = Show.apply[String]

  val intAsString = showInt.show(123)
  val stringAsString = showString.show("abc")

}


