package scalaCheck


sealed trait Account

case class User(id: Int, name: String) extends Account

case class Admin(id: Int, name: String) extends Account

class AccountMethods {

  def weeby(account: Account) = {
    account match {
      case User(id, "Kouhai") if id <= 9000 => "A Kawaii Kouhai UwU"
      case User(id, "Senpai") if id <= 9000 => "Dreamy Senpai..."
      case User(id, _) if id > 9000 => "It's over 9000!!"
      case Admin(id, _) if id <= 100 => "Run it's a manager!!"
      case _ => "Mikey ya Fuckin' Weeb"
    }
  }

}
