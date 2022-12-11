package rockTheJVM

import cats.Eval
import cats.data.IndexedStateT

object FunctionalState {

  type MyState[S, A] = S => (S, A)

  import cats.data.State

  val countAndSay: State[Int, String] = State(currentCount => (currentCount + 1, s"Counted $currentCount"))

  val (eleven, counted10) = countAndSay.run(10).value

  // state = "iterative" computations

  var a = 10

  a += 1
  val firstComputation = s"Added 1 to 10, obtained $a"

  a *= 5
  val secondComputation = s"Multiplied by 5, obtained $a"

  val firstTransformation: State[Int, String] = State((s: Int) => (s + 1, s"Added 1 to 10, obtained ${s + 1}"))
  val secondTransformation: State[Int, String] = State((s: Int) => (s * 5, s"Multiplied by 5, obtained ${s * 5}"))

  val compositeTransformation: IndexedStateT[Eval, Int, Int, (String, String)] =
    firstTransformation.flatMap { firstResult =>
      secondTransformation.map(secondResult => (firstResult, secondResult))
    }

  val compositeTransformation2: IndexedStateT[Eval, Int, Int, (String, String)] = {
    for {
      fst <- firstTransformation
      snd <- secondTransformation
    } yield (fst, snd)
  }

  case class ShoppingCart(items: List[String], total: Double)

  def addToCartAttempt(item: String, price: Double): State[ShoppingCart, Double] =
    State {
      (s: ShoppingCart) => (s.copy(items = s.items :+ item, total = s.total + price), s.total + price)
    }

  def addToCartAnswer(item: String, price: Double): State[ShoppingCart, Double] =
    State {
      (cart: ShoppingCart) => (ShoppingCart(item +: cart.items, cart.total + price), cart.total + price)
    }

  // IndexedStateT[Eval, ShoppingCart, ShoppingCart, Double]  ==  State[ShoppingCart, Double]  type wise I think


  val danielsCart: State[ShoppingCart, Double] =
    for {
      _ <- addToCartAnswer("Fender guitar", 500)
      _ <- addToCartAnswer("Elixir strings", 19)
      total <- addToCartAnswer("Electric cable", 8)
    } yield total


  def main(args: Array[String]) = {

    println(compositeTransformation.run(10).value)
    println(compositeTransformation2.run(20).value)

    println(danielsCart.run(ShoppingCart(List(), 0)).value)
  }

}
