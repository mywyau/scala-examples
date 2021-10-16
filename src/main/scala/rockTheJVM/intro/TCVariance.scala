package rockTheJVM.intro


import cats.instances.int._
import cats.instances.option._
import cats.syntax.eq._

object TCVariance extends App {

  val aComparison = Option(2) === Option(3)

  //  val anInvalidEq = Some(2) === None  -- not valid Eq[Option[Int]] not found

  // variance - generic type subtyping/supertyping propagation

  // covariant type

  class Cage[+T]

  class Animal

  class Cat extends Animal

  //  val cage:Cage[Cat] = new Cage[Animal]  // Cat <: Animal, then Vet[Animal] <: Vet[Cat]

  val cage2: Cage[Animal] = new Cage[Cat]

  // contravariant type - subtyping is propagating backwards to the generic type

  class Vet[-T]

  val vet: Vet[Cat] = new Vet[Animal] // Cat <: Animal, then Vet[Animal] <: Vet[Cat]  Vet[Animal] can heal any animal so it's fine even though we need a vet for cats - Vet[Cat]

  // rule of thumb: "HAS a T" = covariant, "ACTs on T" = contravariant
  // variance affects how TC instances are being fetched


  //contravariant TC
  trait SoundMaker[-T]

  implicit object AnimalSoundMaker extends SoundMaker[Animal]

  def makeSound[T](implicit soundMaker: SoundMaker[T]): Unit = println("wow") //implementation not important

  makeSound[Animal]
  makeSound[Cat] // only have soundmaker of Animal  SoundMaker[Animal] which works as a proper substitute for SoundMaker[Cat] it is a suitable covariant SoundMaker

  implicit object OptionSoundMaker extends SoundMaker[Option[Int]]

  makeSound[Option[Int]]
  makeSound[Some[Int]]

  // covariant TC

  trait AnimalShow[+T] {
    def show: String
  }

  implicit object GeneralAnimalShow extends AnimalShow[Animal] {
    override def show: String = "animals everywhere"
  }

  implicit object CatsShow extends AnimalShow[Cat] {
    override def show: String = "so many cats!"
  }

  def organizeShow[T](implicit event: AnimalShow[T]): String = event.show

  println(organizeShow[Cat]) // uses CatShow only TC applicable so compiler will inject CatShow as implicit

  //  println(organizeShow[Animal])
  /*
    rip it does not compile work why? because compiler can see two valid TC instances for the type AnimalShow[Animal] both

        implicit object CatsShow extends AnimalShow[Cat] and
        implicit object GeneralAnimalShow extends AnimalShow[Animal]  are suitable for running

        println(organizeShow[Animal])    -  you can only have a single implicit or the compiler gets confused

   */


  // rule2: covariant TCs will always use the more specific TC instance for that type
  // but may confuse the compiler if the general TC is also present

  // rule 3: you can't have both benefits
  // Cats uses Invariant TCs

  val compareNone = Option(2) === Option.empty[Int] // alternative to None

}
