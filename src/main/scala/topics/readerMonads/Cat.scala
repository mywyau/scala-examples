package topics.readerMonads

import cats.data.Reader
import topics.readerMonads.CatReader.{checkSound, greetAndSound}

case class Cat(sound: String)

object CatReader {

  // extracting the sound from the Cat class
  val retrieveSound: Reader[Cat, String] = Reader { cat =>
    cat.sound
  }

  println(retrieveSound.run(Cat("Bark")))

  val checkSound: Reader[Cat, Boolean] = retrieveSound.map(_ == "meow") // using map on a Reader type

  val greet: Reader[Cat, String] = Reader { cat => s"hello $cat " }

  val sounds: Reader[Cat, String] = Reader { cat => s"${cat.sound} ${cat.sound}" }

  val greetAndSound =
    for {
      g <- greet
      check <- checkSound
      s <- sounds
    } yield {
      if (check) g + s else "sound is not right"
    }

}

object CatRunner extends App {

  // extracting the sound from the Cat class
  val retrieveSound: Reader[Cat, String] = Reader { cat =>
    cat.sound
  }

  val result = greetAndSound.run(Cat("meow"))

  println(result)

  val notRight = greetAndSound.run(Cat("bark"))
  println(notRight)

//  println(retrieveSound.run(Cat("Meow")))
//  println(checkSound.run(Cat("bark")))
//  println(checkSound.run(Cat("meow")))

}
