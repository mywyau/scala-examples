package example_2

import cats.Monad
import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.flatMap._
import cats.syntax.functor._

trait Console[F[_]] {
  def putStrLn(line: String): F[Unit]
  def readLn: F[String]
}

object Console {
  def apply[F[_]](implicit console: Console[F]): Console[F] = console
}

class Program[F[_]: Console : Monad] {
  def run: F[Unit] = for {
    _    <- Console[F].putStrLn("What is your name?")
    name <- Console[F].readLn
    _    <- Console[F].putStrLn(s"Hello, $name!")
  } yield ()
}

object ConsoleIO extends Console[IO] {
  def putStrLn(line: String): IO[Unit] = IO(println(line))
  def readLn: IO[String] = IO(scala.io.StdIn.readLine())
}

object Main extends IOApp {

  implicit val consoleIO: Console[IO] = ConsoleIO

  def run(args: List[String]): IO[ExitCode] =
    new Program[IO].run.as(ExitCode.Success)
}
