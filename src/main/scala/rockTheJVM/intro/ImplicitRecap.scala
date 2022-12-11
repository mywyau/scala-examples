package rockTheJVM.intro

object ImplicitRecap {

  // implicit classes - one argument wrappers

  case class Person(name: String) {
    def greet = s"Hi my name is $name"
  }

  implicit class ImpersonableString(name: String) {

    // "gives String the greet method" - extension method pattern enriches existing types with new methods

    def greet: String = Person(name).greet
  }

  //  val impersonableString: ImpersonableString = new ImpersonableString("Peter")
  //  impersonableString.greet

  val greetings = "Peter".greet

  import scala.concurrent.duration._ // gives Int the second method

  val oneSec = 1.second

  def increment(x: Int)(implicit amount: Int) = x + amount

  implicit val defaultAmount = 10

  val increment2 = increment(2) //scala compiler magic will find the implicit

  def multiply(x: Int)(implicit times: Int) = x * times

  val times2 = multiply(2)

  trait JSONDeserializer[T] {
    def toJson(value: T): String
  }

  def listToJson[T](list: List[T])(implicit serializer: JSONDeserializer[T]): String =
    list.map(value => serializer.toJson(value)).mkString("[", ",", "]")

  implicit val personSerializer: JSONDeserializer[Person] = {

    // if this is commented out   `val personsJson = listToJson(List(Person("Alice"), Person("Bob")))` breaks

    new JSONDeserializer[Person] {
      override def toJson(person: Person): String =
        s"""
           |{"name": s"${person.name}"}
           |""".stripMargin
    }
  }

  val personsJson = listToJson(List(Person("Alice"), Person("Bob")))
  // implicit argument is used to PROVE THE EXISTENCE of a type


  // implicit methods - all case classes extend the type Product

  implicit def oneArgCaseClassSerializer[T <: Product]: JSONDeserializer[T] = new JSONDeserializer[T] {

    // implicit method is used to PROVE THE EXISTENCE of a type

    override def toJson(value: T) =
      s"""
         |{"${value.productPrefix.toLowerCase}Name": "${value.productElement(0)}"}
         |""".stripMargin.trim
  }

  case class Cat(catName: String)

  val catsToJson =
    listToJson(
      List(Cat("Tom"), Cat("Jonathan"))
    )

  def main(args: Array[String]): Unit = {

    println(oneArgCaseClassSerializer[Cat].toJson(Cat("Garfield")))
    println(oneArgCaseClassSerializer[Person].toJson(Person("David")))
    println(catsToJson)
  }

  //implicits scope order
  //1. local scope
  //2. imported scope
  //3. companion objects of the types involved in the method call

  //example of number 3
  val companionObjectImplicitExample = List(1, 2, 3).sorted // .sorted actually looks for a implicit Ordering type it actually looks into List() companion object


}
