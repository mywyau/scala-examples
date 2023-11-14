package scala.coursera.week1

class CountingChange {

  val coins = List(5, 10, 20, 50, 100, 200, 500)

  //3 2 ways
  // 1+1+1
  // 1+2

  //4 3 ways
  // 1+1+1+1
  // 1+1+2
  // 2+2

  //5  4 ways
  // 1+1+1+1+1
  // 1+1+1+2
  // 1+2+2
  // 1+1+1+1+1

  //6 4 ways
  // 1+1+1+1+1+1
  // 1+1+1+1+2
  // 1+1+2+2
  // 2+2+2

  def countChange(money: Int, coins: List[Int]): Int = {

    def coinHelper(coins: List[Int], amount: Int): Int = {
      amount match {
        case change if change < 0 | coins.isEmpty => 0
        case 0 => 1
        case _ => coinHelper(coins.tail, amount) + coinHelper(coins, amount - coins.head)
      }
    }

    coinHelper(coins, money)
  }
}
