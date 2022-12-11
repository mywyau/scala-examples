package rockTheJVM.intro

object CatsIntro extends App {

  //Eq

  val aComparison = 2 == "a String"

  // part 1 - import your desired typeclass type

  import cats.Eq

  // part 2 - import your typeclass's instances for a type e.g. Int instance for Eq

  import cats.instances.int._

  // part 3 - use the TC API

  val intEquality = Eq[Int]

  val aTypeSafeComparison = intEquality.eqv(2, 3) //false

  //  val aTypeSafeComparison2 = intEquality.eqv(2, "2")  -- won't even compile since Eq is typesafe

  // part 4 use extension methods (if applicable)

  import cats.syntax.eq._ // brings all the extension methods the type supports in our case Eq

  // we will import the Eq operand

  val anotherTypeSafeComparison = 2 === 3 // false
  val neqTypeSafeComparison = 2 =!= 3 // true

  //  val invalidComparison = 2 === "a String"   -- will not even compile again!

  // part 5 - extendind the TC operations to composite types, e.g. List

  import cats.instances.list._ // needs an import, Eq[List[Int]]] now in scope

  val aListComparison = List(2) === List(3)

  // === is only available for a type if the right TC instance is present in scope

  println(aListComparison)
  println(neqTypeSafeComparison)

  // part 6 - unsupported types

  case class ToyCar(model: String, price: Double) //note never actually use Double type for money

  // we will try to Eq ToyCars on the requirement that both ToyCars have the same price

  implicit val toyCarEq: Eq[ToyCar] =
    Eq.instance[ToyCar] { (car1, car2) => car1.price == car2.price }


  val compareTwoToyCars = ToyCar("Ferarri", 29.99) === ToyCar("Lamborghini", 29.99) // true
  val neqTwoToyCars = ToyCar("Ferarri", 30.00) === ToyCar("Lamborghini", 29.99) // false because of our rule/law for equality

  println(s"neqTwoToyCars: are these cars of price $$30.00 and $$£29.99 equal? $neqTwoToyCars")   //haha forgot $ dollar signs need double $$

}
