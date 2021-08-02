import scala.util.Try

class CoinCounter {

  val ukCurrency: Seq[Int] = Seq(1, 2, 5, 10, 20, 50, 100, 200)

  def coins(amount: String) = {

    val amountInPennies: Option[Int] = Try(amount.toInt).toOption

    amountInPennies.map(amount =>

      ukCurrency.map(i =>

        amount % i
      )

    )
  }

}
