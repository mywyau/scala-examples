package specBase

import org.scalatest.{FlatSpec, Matchers}

trait SpecBase extends FlatSpec with Matchers {

  /*
    Stuff you want other tests can have go here

    A spec base is good practice to keep all your tests specs consistent by extending it.
    For example every test spec extending this spec base now has 'FlatSpec with Matchers' functionality.

  */


}
