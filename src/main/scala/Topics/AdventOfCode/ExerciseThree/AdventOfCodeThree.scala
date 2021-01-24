package Topics.AdventOfCode.ExerciseThree

case class PasswordModel(min: Int, max: Int, letter: String, password: String)

class AdventOfCodeThree {

  val input: String = AdventOfCodeThreeInput.input
  val firstRow: String = "..#.......#..##...#...#..#.#..."
  val firstRowCount: Int = "..#.......#..##...#...#..#.#...".size
  val splitIntoSeqs = input.slice(0, firstRowCount)
  val splitIntoSeqs2: Stream[String] = input.grouped(size = 31).toStream
  val splitIntoSeqs3: Seq[Stream[String]] = splitIntoSeqs2.map(_ => splitIntoSeqs2)

}

object ExerciseThree extends App {

  val adventOfCodeThree = new AdventOfCodeThree

  println(adventOfCodeThree.input.size)
  println(adventOfCodeThree.firstRowCount)
  println(adventOfCodeThree.splitIntoSeqs3)

  //  println(adventOfCodeThree.splitIntoSeqs2.)
}
