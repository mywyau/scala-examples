package calculator

class Animal(name:String) {

  def speak() = name

  def eat() = ???

  def walk() = ???




}


object AnimalApp extends App {


  val aCat = new Animal("momo")

  val aFish = new Animal("momo")

  object Momo {

    // one time use

    val name = "Momo"

    val age = 1
  }



  println(Momo.age)

  aCat.speak() // "momo"
  aCat.eat() // "momo"
  aCat.walk() // "momo"

}
