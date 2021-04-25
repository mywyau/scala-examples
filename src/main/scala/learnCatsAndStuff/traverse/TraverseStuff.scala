//package learnCatsAndStuff.traverse
//
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent._
//import scala.concurrent.duration._
//
//
//class TraverseStuff {
//
//  // Look at the Scala with Cats Chapter 7
//
//  /*
//    foldLeft and foldRight are flexible itera􀦞on methods but they require us
//    to do a lot of work to define accumulators and combinator func􀦞ons. The
//    Traverse type class is a higher level tool that leverages Applicatives to
//    provide a more convenient, more lawful, pa􀂂ern for itera􀦞on.
//
//  */
//
//  val hostNames = List(
//    "alpha.example.com",
//    "beta.example.com",
//    "gamma.demo.com"
//  )
//
//  def getUpTime(hostName: String): Future[Int] = Future(hostName.length * 60)
//
//  val allUpTimes: Future[List[Int]] = hostNames.foldLeft(Future(List.empty[Int])) {
//
//    (accum, host) =>
//      val upTime: Future[Int] = getUpTime(host)
//      for {
//        accum: Seq[Int] <- accum // this is a Future[List[Int]]
//        uptime: Int <- upTime // these two are Futures
//      } yield accum :+ uptime
//  }
//
//  Await.result(allUpTimes, 1.seconds)
//
//  // some native Scala solution to a common pattern
//
//  val allUpTimes2: Future[List[Int]] = Future.traverse(hostNames)(getUpTime) // this does the same as our foldLeft
//
//
//  /* Implementation of Future.traverse  which is super samey as our foldLeft implementation
//      def traverse[A, B](values: List[A])
//                      (func: A => Future[B]): Future[List[B]] =
//      values.foldLeft(Future(List.empty[B])) { (accum, host) =>
//        val item = func(host)
//        for {
//          accum <- accum
//          item <- item
//        } yield accum :+ item
//      }
//   */
//
//  // The other method is Future.sequence to handle List[Future[A]]
//
//
//  /*
//    Future.traverse and Future.sequence solve a very specific problem:
//     they allow us to iterate over a sequence of Futures and accumulate a result.
//    The simplified examples above only work with Lists, but the real Future.
//    traverse and Future.sequence work with any standard Scala collec-
//    􀦞on.
//
//    Cats’ Traverse type class generalises these pa􀂂erns to work with any type
//    of Applicative: Future, Option, Validated, and so on. We’ll approach
//    Traverse in the next sec􀦞ons in two steps: first we’ll generalise over the
//    Applicative, then we’ll generalise over the sequence type. We’ll end up
//    with an extremely valuable tool that trivialises many opera􀦞ons involving sequences
//    and other data types.
//
//   */
//
//  /*
//    Traversing with Applica􀦞ves
//    If we squint, we’ll see that we can rewrite traverse in terms of an Applicative.
//    Our accumulator from the example above:  Look at code below
//  */
//
//  import cats.Applicative
//  import cats.instances.future._ // for Applicative
//  import cats.syntax.applicative._ // for pure
//
//  val hmm = Future(List.empty[Int])
//
//  val hmm2 = List.empty[Int].pure[Future]
//
//  import cats.syntax.apply._ // for mapN
//
//  // Combining accumulator and hostname using an Applicative:
//  def newCombine(accum: Future[List[Int]],
//                 host: String): Future[List[Int]] =
//    (accum, getUpTime(host)).mapN((i: List[Int], j: Int) => i :+ j)
//
//  /*
//    By subs􀦞tu􀦞ng these snippets back into the defini􀦞on of traverse we can
//    generalise it to to work with any Applicative:
//  */
//
//  import scala.language.higherKinds
//
//
//
//
//}
