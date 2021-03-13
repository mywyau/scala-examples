package topics.klesliArrows

import cats.Functor

//Copied bits from Cats Library

class KleisliOne {

  val twice: Int => Int = { x => x * 2 }

  val countCats: Int => String = {
    x => if (x == 1) "1 cat" else s"$x cats"
  }

  val twiceAsManyCats: Int => String = {
    twice andThen countCats // equivalent to: countCats compose twice, an example of function composition f(g(x)) i.e. countCats(twice(x))
  }

  twiceAsManyCats(1) // "2 cats"

  //Sometimes, our functions will need to return monadic values. For instance, consider the following set of functions.

  val parse: String => Option[Int] = s => if (s.matches("-?[0-9]+")) Some(s.toInt) else None //returns an Option[Int]

  val reciprocal: Int => Option[Double] = { // Needs a Int from 'val parseStringToInt' but 'val parseStringToInt' gives back Option[Int] how can we solve this?
    yourInt => if (yourInt != 0) Some(1.0 / yourInt) else None
  }

  /*
    As it stands we cannot use Function1.compose (or Function1.andThen) to compose these two functions.
    The output type of parse is Option[Int] whereas the input type of reciprocal is Int.
    This is where Kleisli comes into play.
  */

  /*
    At its core, Kleisli[F[_], A, B] is just a wrapper around the function A => F[B]. Depending on the properties of the F[_] (F being the 'context' for example a monad),
    we can do different things with Kleisli. For instance, if F[_] has a FlatMap[F] instance (we can call flatMap on F[A] values),
    we can compose two Kleisli much like we can two functions
  */

  // Bring in cats.FlatMap[Option] instance --- below is implementation of Kleisli i.e. A => F[B]

  import cats.FlatMap
  import cats.implicits._

  final case class Kleisli[F[_], A, B](run: A => F[B]) {

    def compose[Z](kleisli: Kleisli[F, Z, A])(implicit F: FlatMap[F]): Kleisli[F, Z, B] = {
      Kleisli[F, Z, B]((z: Z) => kleisli.run(z).flatMap(run))
    }

    /*
      Related to stuff below for some F[_] which do not have a FlatMap, e.g. is F[_] was a functor which can not have a flatMap. This means we can use Kleisli
      with F[_] that have weaker requirements and still do a A => F[B] for a functor.
    */
    def map[C](f: B => C)(implicit F: Functor[F]): Kleisli[F, A, C] = {
      Kleisli[F, A, C](a => F.map(run(a))(f))
    }

  }

  // (Original above functions) cannot compose or andThen these since they are monadic

  /*  val parseStringToOptInt: String => Option[Int] = s => if (s.matches("-?[0-9]+")) Some(s.toInt) else None   <---- String => Option[Int]
      val reciprocal: Int => Option[Double] = i => if (i != 0) Some(1.0 / i) else None   <------ functions from Int => Option[Double]
      We will alter the implementation into a implementation involving 'Kleisli[F[_], A, B]'
  */

  val parseK: Kleisli[Option, String, Int] = Kleisli((s: String) => if (s.matches("-?[0-9]+")) Some(s.toInt) else None) //wrap them in our Kleisli implementation

  val reciprocalK: Kleisli[Option, Int, Double] = Kleisli((i: Int) => if (i != 0) Some(1.0 / i) else None)

  val parseAndReciprocal: Kleisli[Option, String, Double] = reciprocalK.compose(parseK) //Now we can compose them, since Kleisli handles the option

  /*
    It is important to note that the F[_] having a FlatMap (or a Monad) instance is not a hard requirement - we can do useful things with weaker requirements.
    Such an Topics.example would be Kleisli#map, which only requires that F[_] have a Functor instance (e.g. is equipped with map: F[A] => (A => B) => F[B]).
  */

  //I have placed the map function for Kleisli in the above Kleisli final case class to be neat

  /*
  Other uses - Monad Transformers

    Many data types have a monad transformer equivalent that allows us to compose the Monad instance of the data type with any other Monad instance.
    For instance, OptionT[F[_], A] allows us to compose the monadic properties of Option with any other F[_], such as a List.
    This allows us to work with nested contexts/effects in a nice way (for Topics.example, in for-comprehensions).
    Kleisli can be viewed as the monad transformer for functions.

    Recall that at its essence, Kleisli[F, A, B] is just a function A => F[B], with niceties to make working with the value we actually care about, the B, easy.
    Kleisli allows us to take the effects of functions and have them play nice with the effects of any other F[_].

    This may raise the question, what exactly is the “effect” of a function?
    Well, if we take a look at any function, we can see it takes some input and produces some output with it,
    without having touched the input (assuming the function is pure, i.e. referentially transparent). That is, we take a read-only value, and produce some value with it.

    For this reason, the type class instances for functions often refer to the function as a Reader. For instance, it is common to hear about the Reader monad.
    In the same spirit, Cats defines a Reader type alias along the lines of:
  */

  // We want A => B, but Kleisli provides A => F[B]. To make the types/shapes match,
  // we need an F[_] ('a computational context') such that providing it a type A is equivalent to A
  // This can be thought of as the type-level equivalent of the identity function

  type Id[A] = A //wrapper to provide a F[_] but all the F[_] ('Id[_]') does is return A but technically it is wrapped in a "computational context"

  type Reader[A, B] = Kleisli[Id, A, B]

  object Reader {
    // Lifts a plain function A => B into a Kleisli, giving us access
    // to all the useful methods and type class instances
    def apply[A, B](f: A => B): Reader[A, B] = Kleisli[Id, A, B](f)

    //Id type alias 'Id[A]' fills the requirement of F[_] for Kleisli we couldn't do Kleisli[A, A, B] since we needed a F[_].
  }

  type ReaderT[F[_], A, B] = Kleisli[F, A, B]

  val ReaderT = Kleisli

  /*
  Case Study - Configuration

  Functional programming advocates the creation of programs and modules by composing smaller, simpler modules.
  This philosophy intentionally mirrors that of function composition - write many small functions, and compose them to build larger ones.
  After all, our programs are just functions.

  Let’s look at some Topics.example modules, where each module has its own configuration that is validated by a function.
  If the configuration is good, we return a Some of the module, otherwise a None.

  This Topics.example uses Option for simplicity - if you want to provide error messages or other failure context,
  consider using Either instead.
   */

  final case class Kleisli2[F[_], Z, A](run: Z => F[A]) {
    def flatMap[B](f: A => Kleisli[F, Z, B])(implicit F: FlatMap[F]): Kleisli[F, Z, B] =
      Kleisli(z => F.flatMap(run(z))(a => f(a).run(z)))

    def map[B](f: A => B)(implicit F: Functor[F]): Kleisli[F, Z, B] =
      Kleisli(z => F.map(run(z))(f))

    def local[ZZ](f: ZZ => Z): Kleisli[F, ZZ, A] = Kleisli(f.andThen(run))
  }

  trait Database

  case class DbConfig(url: String, user: String, pass: String)

  object Database {
    val fromDbConfig: Kleisli2[Option, DbConfig, Database] = ???
  }

  trait Service

  case class ServiceConfig(address: String, port: Int)

  object Service {
    val fromServiceConfig: Kleisli2[Option, ServiceConfig, Service] = ???
  }

  /*
  We have two independent modules, a 'Database' (allowing access to a database) and a 'Service' (supporting an API to provide data over the web).
  Both depend on their own configuration parameters. Neither know or care about the other, as it should be. However our application needs both of these modules to work.
  It is plausible we then have a more global application configuration.
   */

  case class AppConfig(databaseConfig: DbConfig, serviceConfig: ServiceConfig) //higher level config combining the two other configs

  class App(db: Database, sv: Service) // will create this in the for loop

  /*
  As it stands, we cannot use both Kleisli validation functions together nicely - one takes a DbConfig, the other a ServiceConfig.
  That means the FlatMap (and by extension, the Monad) instances differ (recall the input type is fixed in the type class instances).
  However, there is a nice function on Kleisli called local.
   */

  /*
          *** Needs implementation of db and sv but we have no implementation so unable to run and only a hypothetical example ***

     def appFromAppConfig: Kleisli2[Option, AppConfig, App] = {
        for {
          db <- Database.fromDbConfig.local[AppConfig](_.databaseConfig) //type DbConfig
          sv <- Service.fromServiceConfig.local[AppConfig](_.serviceConfig) //type ServiceConfig
        } yield new App(db, sv)
      }
  */

  /*

    What if we need a module that doesn’t need any config validation, say a strategy to log events?
    We would have such a module be instantiated from a config directly, without an Option - we would have something like
    Kleisli[Id, LogConfig, Log] (alternatively, Reader[LogConfig, Log]). However, this won’t play nice with our other Kleislis since those use Option instead of Id.

    We can define a lift method on Kleisli (available already on Kleisli in Cats) that takes a type parameter G[_]
    such that G has an Applicative instance and lifts a Kleisli value such that its output type is G[F[B]].
    This allows us to then lift a Reader[A, B] into a Kleisli[G, A, B].
    Note that lifting a Reader[A, B] into some G[_] is equivalent to having a Kleisli[G, A, B] since Reader[A, B] is just a type alias
    for Kleisli[Id, A, B], and type Id[A] = A so G[Id[A]] is equivalent to G[A].

  */
}
