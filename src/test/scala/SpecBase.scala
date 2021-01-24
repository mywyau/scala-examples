import org.scalatest.{FlatSpec, Matchers}

abstract class SpecBase(component: String) extends FlatSpec with Matchers {

  behavior of component

}
