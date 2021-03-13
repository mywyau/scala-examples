package topics.selfTypes

trait User {
  def username: String
}

trait Tweeter {
  this: User => // the reassignment of 'this' makes 'def username: String' in scope of Tweeter
  def tweet(tweetText: String): Unit = println(s"$username: $tweetText") //username is from User trait
}

class VerifiedTweeter(username_ : String) extends Tweeter with User { // we mixin User because Tweeter requires it
  def username = s"real $username_"
}
