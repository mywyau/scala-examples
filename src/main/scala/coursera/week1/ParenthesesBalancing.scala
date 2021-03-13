package coursera.week1

import scala.annotation.tailrec

class ParenthesesBalancing {

  //  def balance(chars: List[Char]): Boolean = {  < ------- ***** failure *****
  //    val open: Char = "(".charAt(0)
  //    val close: Char = ")".charAt(0)
  //
  //    val onlyParens: List[Char] = chars.filter(x => x == open | x == close)
  //    println(onlyParens.toString + s"****** only parens  ${onlyParens.size}")
  //
  //    @tailrec
  //    def balanceHelper(parens: List[Char], open: List[Char]): Boolean = {
  //
  //      val countOpen: Int = open.count(x => x == open)
  //      val countClose: Int = open.count(x => x == close)
  //
  //      val isEven: Boolean = if (countOpen == countClose) true else false
  //
  //      val evenParens = open match {
  //        case i if i.isEmpty => true
  //        case i if i.nonEmpty => isEven
  //        case _ => false
  //      }
  //
  //      // "(, (, ), (, ), )"
  //
  //      parens match {
  //        case c if c.isEmpty =>
  //          println("empty")
  //          println(open)
  //          evenParens
  //        case c :: cs if c == open && cs.contains(close) =>
  //          println(open + "case 1")
  //          balanceHelper(cs.take(cs.size - 1), open ++ "(".toList ++ ")".toList)
  //        case c :: cs if c == close && cs.contains(open) =>
  //          println(open + "case 2")
  //          balanceHelper(cs.take(cs.size - 1), open ++ "(".toList ++ ")".toList)
  //        case _ =>
  //          println("Boom")
  //          false
  //      }
  //    }
  //
  //    balanceHelper(onlyParens, "".toList)
  //  }

  def balance(chars: List[Char]): Boolean = {

    val openParen: Char = "(".charAt(0)
    val closeParen: Char = ")".charAt(0)
    val onlyParens: List[Char] = chars.filter(x => x == openParen | x == closeParen)

    @tailrec
    def balanceHelper(parens: List[Char], open: Int): Boolean = {
      parens match {
        case c if c.isEmpty => open == 0
        case c :: cs if c == openParen => balanceHelper(cs, open + 1)
        case c :: cs if c == closeParen => open > 0 && balanceHelper(cs, open - 1)
        case _ => false
      }
    }

    balanceHelper(onlyParens, 0)
  }

}
