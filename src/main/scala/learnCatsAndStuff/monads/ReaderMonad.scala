package learnCatsAndStuff.monads

import cats.data.Reader
import cats.{Applicative, Id, _}

case class Cat(name: String, favourtiteFood: String)

class ReaderMonad {

  /*  We can create a Reader[A, B] from a function A => B using the
    Reader.apply constructor:*/

  val catReader: Reader[Cat, String] = Reader(cat => cat.name) //Create a Reader of Cats, by defining it's type and using it's apply to construct it for our Cat case class

  val catName: Id[String] = catReader.run(Cat(name = "Myau", favourtiteFood = "Pineapple Pizza"))
  // res0: cats.Id[String] = Myau

  // So far so simple, but what advantage do Readers give us over the raw functions?

  /*  The power of Readers comes from their map and flatMap methods, which
    represent different kinds of function composition. We typically create a set of
      Readers that accept the same type of configuration, combine them with map
    and flatMap, and then call run to inject the config at the end.*/

  val greetKitty: Reader[Cat, String] = catReader.map(name => s"Hello $name") // type can also be  Kleisli[Id, Cat, String], since Reader[A, B] is a Kleisli Category

  val sayHello = greetKitty.run(Cat("Heathcliff", "junk food"))

  val feedKitty: Reader[Cat, String] = Reader(cat => s"have a nice bowl of ${cat.favourtiteFood}")

  val greetAndFeed: Reader[Cat, String] =
    for {
      greet <- greetKitty
      feed <- feedKitty
    } yield s"$greet. $feed."
}

/*The classic use of Readers is to build programs that accept a configuration
as a parameter. Let’s ground this with a complete example of a simple login
system. Our configuration will consist of two databases: a list of valid users
and a list of their passwords:*/

case class Database(usernames: Map[Int, String], passwords: Map[String, String])

class DatabaseExercise {

  import cats.syntax.applicative._ // for pure

  type DbReader[A] = Reader[Database, A]

  def findUsername(userId: Int): DbReader[Option[String]] =
    Reader(db => db.usernames.get(userId))

  def checkPassword(username: String, password: String): DbReader[Boolean] =
    Reader(db => db.passwords.get(username).contains(password))

  def checkLogin(userId: Int, password: String): DbReader[Boolean] =
    findUsername(userId).flatMap(optName => optName.map(name =>
      checkPassword(username = name, password)).getOrElse(Reader(_ => false))  //other way if no using .pure[A]
    )

  def checkLogin2(userId: Int, password: String)(implicit applicative: Applicative[DbReader]): DbReader[Boolean] =
  // book was missing the second set of parameters in method definition for  'implicit applicative: Applicative[DbReader]' so unable to access .pure
    findUsername(userId).flatMap(optName => optName.map(name =>
      checkPassword(username = name, password)).getOrElse(false.pure[DbReader])  // needs the implicit defined in method not just the import
    )

//  val users = Map(
//    1 -> "dade",
//    2 -> "kate",
//    3 -> "margo"
//  )
//
//  val passwords = Map(
//    "dade" -> "zerocool",
//    "kate" -> "acidburn",
//    "margo" -> "secret"
//  )
}

object ReaderRunner extends App {

  val users = Map(
    1 -> "dade",
    2 -> "kate",
    3 -> "margo"
  )

  val passwords = Map(
    "dade" -> "zerocool",
    "kate" -> "acidburn",
    "margo" -> "secret"
  )

  val db = Database(users, passwords)

  val databaseExercise = new DatabaseExercise

  println(db)

  println(databaseExercise.checkLogin(1, "zerocool").run(db))

  println(databaseExercise.checkLogin(4, "davinci").run(db))

}










