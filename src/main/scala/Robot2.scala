import Robot.generateName

import java.util.concurrent.atomic.AtomicInteger

case class Robot(var name: String = generateName()) {
  def reset(): Unit = name = generateName()
}

object Robot {

  private val alphabetUpperCase = 'A' to 'Z'
  private val validNumbers = 100 to 999
  private val currentIndex = new AtomicInteger(0)

  private val allRobotNames: Seq[String] = {

    for {
      a <- alphabetUpperCase
      b <- alphabetUpperCase
      c <- validNumbers
    } yield {
      s"$a$b$c"
    }
  }

  def generateName(): String = allRobotNames(currentIndex.getAndIncrement())
}