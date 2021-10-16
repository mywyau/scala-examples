package scalamock

import org.scalatest.matchers.must.Matchers._
import specBase.WordSpecScalaMockBase

class GreetingsSpec extends WordSpecScalaMockBase {

  val greetings = new Greetings()

  val mockFormatter: Formatter = mock[Formatter]

  "Greetings" when {

    ".sayHello()" should {

      "return the correct message" in {

        (mockFormatter.format(_: String))
          .expects("Mr Mikey")
          .returns("Ah, Mr Bond. I've been expecting you")
          .once()

        //        (mockFormatter.format _)  // alternative syntax kinda
        //          .expects("Mr Mikey")
        //          .returning("Ah, Mr Mikey. I've been expecting you")
        //          .once()   // number of times expected call

        //        val result = greetings.sayHello("Mr Bond", mockFormatter)  // this will kill it since we mocked it out with 'Mikey' not 'Bond'
        val result = greetings.sayHello("Mr Mikey", mockFormatter)

        result mustBe "Ah, Mr Bond. I've been expecting you"

      }
    }


    ".sayHello() v2" should {

      "return the correct message" in {

        val mockFormatter = stub[Formatter]
        val bond = "Mr Bond"
        val mikey = "Mr Mikey"

        (mockFormatter.format _).when(bond).returns("Ah, Mr Bond. I've been expecting you")

        greetings.sayHello(bond, mockFormatter)

        //        (mockFormatter.format _).verify(mikey).once()
      }
    }

    "throwing exceptions" in {

      //  throwing an exception in a mock

      val brokenFormatter = mock[Formatter]

      (brokenFormatter.format _).expects(*).throwing(new NullPointerException).anyNumberOfTimes()

      intercept[NullPointerException] {
        greetings.sayHello("Erza", brokenFormatter)
      }
    }

    "dynamically responding to a parameter with onCall" in {

      val australianFormat = mock[Formatter]

      (australianFormat.format _).expects(*).onCall { s: String => s"G'day $s" }

      greetings.sayHello("Wendy", australianFormat)
      //      greetings.sayHello("Gray", australianFormat)
    }
  }

}

