package topics.selfTypes

object Runner extends App {

  val beyonce = new VerifiedTweeter("Beyonce")

  println(beyonce.username) // prints "real Beyonce"
  beyonce.tweet("Just spilled my glass of lemonade") // prints "real Beyonce: Just spilled my glass of lemonade" from the Tweeter trait
}
