package specBase

import org.scalamock.scalatest.MockFactory
import org.scalatest.wordspec.AnyWordSpecLike

trait WordSpecScalaMockBase extends AnyWordSpecLike with MockFactory {

  /*
    Stuff you want other tests can have go here

    A spec base is good practice to keep all your tests specs consistent by extending it.
    For example every test spec extending this spec base now has 'FlatSpec with Matchers' functionality.

  */


}
