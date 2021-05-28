package coursera.week1

import specBase.SpecBase

class ParenthesesBalancingFlatSpecMockito extends SpecBase {

  val exerciseTwo = new ParenthesesBalancing

  behavior of ".balance(chars)"

  "a balanced parentheses List[Chars]: \"(if (zero? x) max (/ 1 x))\"" should "return true" in {

    val characters: List[Char] = "(if (zero? x) max (/ 1 x))".toList

    exerciseTwo.balance(characters) mustBe true
  }

  "a balanced parentheses List[Chars]: \"I told him (that it’s not (yet) done). (But he wasn’t listening)\"" should "return true" in {

    val characters: List[Char] = "I told him (that it’s not (yet) done). (But he wasn’t listening)".toList

    exerciseTwo.balance(characters) mustBe true
  }

  "a unbalanced parentheses List[Chars]: \":-)\"" should "return false" in {

    val characters: List[Char] = ":-)".toList

    exerciseTwo.balance(characters) mustBe false
  }

  "a unbalanced parentheses List[Chars]: \"())(\" " should "return false" in {

    val characters: List[Char] = "())(".toList

    exerciseTwo.balance(characters) mustBe false
  }

}
