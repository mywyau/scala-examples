package rockTheJVM.intro

object TypeClasses {

  case class Person2(name: String, age: Int)

  //type class definition - a pattern to enhance a type

  // part 1 - type class definition

  trait JSONSerializer2[T] {
    def toJson(value: T): String
  }

  //part2 - create implicit type class Instances

  implicit object StringSerializer extends JSONSerializer2[String] {
    override def toJson(value: String): String = "\"" + "\""
  }

  implicit object IntSerializer extends JSONSerializer2[Int] {
    override def toJson(value: Int): String = value.toString
  }

  implicit object PersonSerializer extends JSONSerializer2[Person2] {
    override def toJson(value: Person2): String =
      s"""
         |{"name": s"${value.name}", "age": ${value.age}}
         |""".stripMargin.trim
  }

  // part 3 - offer some API

  def convertListToJSON[T](list: List[T])(implicit serializer: JSONSerializer2[T]): String =
    list.map(v => serializer.toJson(v)).mkString("[", ",", "]")

  val printPersonListAsJSON = convertListToJSON(List(Person2("Alice", 23), Person2("Xavier", 45)))

  // part 4 - extending the existing types via extension methods

  object JSONSyntax {

    implicit class JSONSerializable[T](value: T)(implicit serializer: JSONSerializer2[T]) {

      def toJson: String = serializer.toJson(value)

    }
  }

  import JSONSyntax._ //needed to get the .toJson extension method

  val bob = Person2("Bob", 35)

  val jsonBob = bob.toJson

}
