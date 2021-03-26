package topics.extensionPattern

class Animal(val name: String) {

  def walk() = "I am walking"

  def talk() = s"Hello I am a $name"

  def play() = "I want to play a game"

  def run() = "I'm fast as fuck boi"

}

object MikeyExtensions {

  implicit class extendFunctionality(animal: Animal) {

    def reverseName(): String =  animal.name.reverse

    def reverseSpeech(): String =  animal.talk().reverse
  }
}

object ClassExtensionRunner extends App {

  import  MikeyExtensions._

  val animal = new Animal("Tiger")

  println(animal.name)

  println(animal.talk())  //talk() is an original method part of the Animal class

  println(animal.reverseSpeech())  //reverseSpeech() is NOT an original method part of the Animal class but was added in

  println(animal.reverseName())   // so reverse name was not part of the Animal class but is an extension via the MikeyExtensions object
                                  // the implicit class picks up the animal and then lets you use the .reverseName() in a style similar to if it was part of the original class

}


