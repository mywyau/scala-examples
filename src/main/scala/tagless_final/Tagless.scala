package tagless_final

import cats.Monad
import cats.effect.{ExitCode, IOApp}


object Tagless extends IOApp {

  import cats.effect.Sync

  // 1. The algebraic data type

  trait ExampleDSL[F[_]] {

    def readString: F[String]

    def writeString(str: String): F[Unit]
  }

  object ExampleDSL {

    def apply[F[_] : Sync]: ExampleDSL[F] =
      new ExampleDSL[F] {
        override def readString: F[String] = Sync[F].delay(scala.io.StdIn.readLine())

        override def writeString(str: String): F[Unit] = Sync[F].delay(println(str))
      }
  }

  object ExampleDSL {

    def apply2[F[_] : Monad]: ExampleDSL[F] =
      new ExampleDSL[F] {
        override def readString: F[String] = Monad[F].pure(scala.io.StdIn.readLine())

        override def writeString(str: String): F[Unit] = Monad[F].pure(println(str))
      }
  }

  // 2. The Interpreter

  import cats.effect.IO

  object ExampleInterpreter extends ExampleDSL[IO] {

    override def readString: IO[String] = IO(scala.io.StdIn.readLine())

    override def writeString(str: String): IO[Unit] = IO(println(str))
  }

  object ExampleInterpreterOption extends ExampleDSL[Option] {

    override def readString: Option[String] = Option(scala.io.StdIn.readLine())

    override def writeString(str: String): Option[Unit] = Option(println(str))
  }


  // Main
  def run(args: List[String]): IO[ExitCode] = {

    val program2: Option[Unit] =
      for {
        _ <- ExampleDSL[Option].writeString("Option Enter a string:")
        strOpt <- ExampleDSL[Option].readString
        _ <- ExampleDSL[Option].writeString(s"Option You entered: $strOpt")
      } yield ()


    val program =
      for {
        _ <- ExampleDSL[IO].writeString("Enter a string:")
        str <- ExampleDSL[IO].readString
        _ <- ExampleDSL[IO].writeString(s"You entered: $str")
      } yield ()

    program.as(ExitCode.Success)
  }
}
