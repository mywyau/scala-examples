package weaverTest

import cats.effect._
import weaver.SimpleIOSuite

import java.util.UUID

// Suites must be "objects" for them to be picked by the framework
object MySuite extends SimpleIOSuite {

//  pureTest("non-effectful (pure) test") {
//    expect("hello".length == 6)
//  }

  private val random: IO[UUID] = IO(java.util.UUID.randomUUID())

  private val sleepOneSecondIO: IO[Unit] = IO(Thread.sleep(1000))

  private val sleepOneSecondIOThenGenerateRandomValue: IO[UUID] = IO(Thread.sleep(1000)).flatMap(_ => random)

  test("test with side-effects") {
    for {
      x <- random
      y <- random
    } yield expect(x != y)
  }

  for (n <- 1 to 100) {
    test(s"Scenario $n out of 100 - test with sleeps") {
      for {
        x <- sleepOneSecondIOThenGenerateRandomValue
//        _ <- sleepOneSecondIO
        y <- sleepOneSecondIOThenGenerateRandomValue
      } yield
        expect(x != y)
    }
  }

  loggedTest("test with side-effects and a logger") { log =>
    for {
      x <- random
      _ <- log.info(s"x : $x")
      y <- random
      _ <- log.info(s"y : $y")
    } yield expect(x != y)
  }
}
