package topics.futures

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class TimeLord()() {

  /*
    Oh noes you have a Sequence of Futures :( this may not be the type signature you want.
    Individual futures in the seq can fail and you will need to handle this
 */
  val sequenceOfFutures: Seq[Future[String]] = ???

  /*
    If we want a Future[Seq[A]] type from our 'val sequenceOfFutures: Seq[Future[String]] = ???',
    We will shuffle 'sequenceOfFutures' into the desired type Future[Seq[A]] using 'Future.sequence()'
  */
  val reshuffleSequenceOfFutures: Future[Seq[String]] = Future.sequence(sequenceOfFutures)


}
