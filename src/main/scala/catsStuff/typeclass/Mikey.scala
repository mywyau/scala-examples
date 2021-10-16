package catsStuff.typeclass

// My own totally original Sum algebraic data type

sealed trait Mikey

final case class MObject(get: Map[String, Mikey]) extends Mikey

final case class MString(get: String) extends Mikey

final case class MInteger(get: Int) extends Mikey

final case class MNumber(get: Double) extends Mikey

final case class MBoolean(get: Boolean) extends Mikey {

  def judgement: String = if (get) "So real" else "So fake"
}

case object MNull extends Mikey

case class Person(name: String, email: String) //custom case class for a custom type

// defining a type class

trait MikeyWriter[Z] {

  def mikeyWrite(value: Z): Mikey // abstract method
}

// implementation of your type class interface/points of usage

/*   Type classes do not come as standard in Scala so you need to make your own

  A Type Class Pattern Recipe

    1. create object as a instance for your type class
    2. implicit val which contains an 'new' instance of your parametric type trait with a concrete type e.g. for Strings or even your own custom types like sushi
    3. produce overridden def since your trait is abstract so needs an implementation of the abstract method
    4. create an interface for your type class

*/

object ObjImplicitMikeyWriterInstance {

  implicit val stringWriter: MikeyWriter[String] = { // can give type annotation
    new MikeyWriter[String] {
      override def mikeyWrite(value: String): Mikey = MString(value)
    }
  }

  //custom type from case class Person

  implicit val personWriter = new MikeyWriter[Person] { //or define a new instance of typeclass where it will use type inference

    override def mikeyWrite(value: Person): Mikey =
      MObject(Map(
        "name" -> MString(value.name),
        "email" -> MString(value.email)
      ))
  }

  implicit val intWriter: MikeyWriter[Int] = {
    new MikeyWriter[Int] {
      override def mikeyWrite(value: Int): Mikey = MInteger(value)
    }
  }

}

trait ImplicitMikeyTrait {

  implicit val booleanWriter: MikeyWriter[Boolean] = { // can give type annotation
    new MikeyWriter[Boolean] {
      override def mikeyWrite(value: Boolean): Mikey = MBoolean(value)
    }
  }
}

// Interface Object - specifying an interface
object MikeyObjectSyntax {

  def objectWayToMikeyType[A](value: A)(implicit mikey: MikeyWriter[A]): Mikey = mikey.mikeyWrite(value)
}

object MikeyInterfaceSyntax { //extension syntax

  implicit class MikeyWriterOps[A](value: A) {

    def interfaceToMikey(implicit mikey: MikeyWriter[A]): Mikey = mikey.mikeyWrite(value)
  }

}

/*
  Scala provides a generic type class interface called 'implicitly'
  We can use implicitly to summon any value from the implicit scope. We provide the type we want and 'implicitly' does the rest

  def implicitly[A](implicit value: A): A = value
*/

object ImplicitlyMethod extends ImplicitMikeyTrait { //  <--- the trait hold the implicit val for type MikeyWriter[Boolean] so we can use via Scala implicitly

  import ObjImplicitMikeyWriterInstance._ // which contains all the implicit implementations of our type class, could make as a a trait to mix in

  val implicitlyMikeyString: Mikey = implicitly[MikeyWriter[String]].mikeyWrite("Oh Noes")

  val implicitlyMikeyInt: Mikey = implicitly[MikeyWriter[Int]].mikeyWrite(1337)

  val implicitlyMikeyPerson: Mikey = implicitly[MikeyWriter[Person]].mikeyWrite(Person(name = "Mokie", email = "MokieMokie@Gmail.com"))

  def implicitMikeyBoolean: Mikey = implicitly[MikeyWriter[Boolean]].mikeyWrite(true)
  // for this method implicitly is able to see the type class instance of .mikeyWrite() for a Bool, the boolean version does not come from the imported object

  // implicitly looks into the ObjImplicitMikeyWriterInstance._ and then lets you use the three implicit values for String, Int, & Person

}

object RunnerA extends App {

  import ImplicitlyMethod._

  println(implicitlyMikeyString)
}


object RunnerB extends App {

  import MikeyInterfaceSyntax._
  import MikeyObjectSyntax.objectWayToMikeyType
  import ObjImplicitMikeyWriterInstance._ //object way

  val objMikeyString: Mikey = objectWayToMikeyType("Tomying away")
  val objMikeyPerson: Mikey = objectWayToMikeyType(Person("Tomy", "tomy@TMail.com"))
  val objMikeyInteger: Mikey = objectWayToMikeyType(1)

  /*
    Scala magic handles the implicits scope technically this is going on in the background --
    '"Tomying away".interfaceToMikey(stringWriter)'  <-- where 'stringWriter' is in the implicit scope
  */

  val interfaceMikeyString: Mikey = "Tomying away".interfaceToMikey

  /*
     Scala magic handles the implicits scope technically this is going on in the background --
     'Person("Tomy", "tomy@TMail.com").interfaceToMikey(personWriter)'  <-- where 'personWriter' is in the implicit scope
  */
  val interfaceMikeyPerson: Mikey = Person("Tomy", "tomy@TMail.com").interfaceToMikey

  /*
   Scala magic handles the implicits scope technically this is going on in the background --
   '9001.interfaceToMikey(intWriter)'  <-- where 'intWriter' is in the implicit scope
*/
  val interfaceMikeyInteger: Mikey = 9001.interfaceToMikey

  println(objMikeyString)
  println(objMikeyPerson)
  println(objMikeyInteger)
}

/*

  A type class interface is any functionality we expose to users. Interfaces are
  generic methods that accept instances of the type class as implicit parameters.

  There are two common ways of specifying an interface: Interface Objects and
  Interface Syntax.

  we can package type class instances in roughly four ways:

  1. by placing them in an object such as JsonWriterInstances;      objects so import to use
  2. by placing them in a trait;                                    could then mixin to use
  3. by placing them in the companion object of the type class;
  4. by placing them in the companion object of the parameter type.

  With option 1 we bring instances into scope by importing them. With option
  2 we bring them into scope with inheritance. With options 3 and 4, instances
  are always in implicit scope, regardless of where we try to use them.

*/
