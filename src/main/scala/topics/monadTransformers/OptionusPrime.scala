package topics.monadTransformers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

// Need the cats imports and implicit._
import cats.data.OptionT
import cats.implicits._

/*
*
* https://blog.buildo.io/monad-transformers-for-the-working-programmer-aa7e981190e7
*
* Before concluding, some word of caution:
* Monad Transformers work really well in some common cases (like this one), but don’t push it too far:
* I don’t advise nesting more than two monads, as it gets really really tricky. For a tragic example, take a look at this README: https://github.com/djspiewak/emm.
* Monad Transformers are not for free, in terms of allocations. There’s a lot of wrapping/unwrapping involved, so if performance is a concern think twice and run some benchmarks.
* Since they’re not standard in the language (there are multiple library implementations available: cats, scalaz and possibly others) don’t expose them as a public API. Call .value on your transformers and expose a regular A[B[X]] which doesn’t impose any opinionated choice on your users and also allows you to swap implementations without introducing a breaking change.
* Finally, Monad Transformers are just one of the possible ways of dealing with stacked Monads.
*
*/

class OptionusPrime() {

  // first implementation of a bunch of methods about mikeys making sushi
  def findMikeyById(id: Int): Future[Mikey] = Future(Mikey(id))

  def makeAFutureSushi(tastinessScore: Mikey): Future[Sushi] = Future(Sushi(tastinessScore))

  def findTheSushiMadeByAMikey(mikeyID: Int): Future[Sushi] = {
    for {
      theSushiChef <- findMikeyById(mikeyID)
      sushi <- makeAFutureSushi(theSushiChef)
    } yield sushi
  }

  /*
    - you then realise there are un-captured mikey's which have not been id-ed.
    - you then also realise that mikeys make random stuff that may not resemble sushi or know how to make sushi so you make it optional
  */

  def findMikeyByIdPrime(id: Int): Future[Option[Mikey]] = Future(Some(Mikey(id)))

  def makeAFutureSushiPrime(mikey: Mikey): Future[Option[Sushi]] = Future(Some(Sushi(mikey)))

  // you still want to taste that legendary sushi but this code doesnt compile, the type signatures is mismatched and do not compose

  /*
    def findTheSushiMadeByAMikeyPrime(mikeyID: Int): Future[Option[Sushi]] = {
        for {
          theSushiChef <- findMikeyByIdPrime(mikeyID)
          sushi <- makeAFutureSushiPrime(theSushiChef)
        } yield sushi
      }
  */

  // you need to handle the option and have to flatMap etc. this is native which handles the option inside the Future

  def findTheSushiMadeByAMikeyPrime(mikeyID: Int): Future[Option[Sushi]] = {
    findMikeyByIdPrime(mikeyID).flatMap {
      case Some(mikey) => makeAFutureSushiPrime(mikey)
      case _ => Future.successful(None)
    }
  }

  // cat-ify the method to make it fancy

  def findTheSushiMadeByAMikeyCats(mikeyID: Int): Future[Option[Sushi]] = {
    (for {
      theSushiChef <- OptionT(findMikeyByIdPrime(mikeyID))
      sushi <- OptionT(makeAFutureSushiPrime(theSushiChef))
    } yield sushi).value
  }

  // Now to clean up everything to make it a little nicer

  def finalFindMikeyById(id: Int): OptionT[Future, Mikey] = OptionT {
    Future(Some(Mikey(id)))
  }

  def finalMakeAFutureSushiPrime(mikey: Mikey): OptionT[Future, Sushi] = OptionT {
    Future(Some(Sushi(mikey)))
  }


  def finalFindTheSushiMadeByAMikeyCats(mikeyID: Int): OptionT[Future, Sushi] = {
    for {
      theSushiChef <- finalFindMikeyById(mikeyID)
      sushi <- finalMakeAFutureSushiPrime(theSushiChef)
    } yield sushi
  }

  // then we can use the final version of our cat-ified method by calling .value on it to ge the desired - Future[Option[Sushi]]

  val getMikeyToMakeMeSushi: Future[Option[Sushi]] = finalFindTheSushiMadeByAMikeyCats(42).value

}


case class Mikey(id: Int)

case class Sushi(chef: Mikey)









