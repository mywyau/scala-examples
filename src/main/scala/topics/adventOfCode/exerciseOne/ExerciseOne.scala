package topics.adventOfCode.exerciseOne

object ExerciseOne extends App {

  val adventOfCode: AdventOfCodeOne = new AdventOfCodeOne()
  val exerciseOneNumbers = adventOfCode.seqOfNumbers
  val findNumbersSumTo2020 = adventOfCode.addToANumber(exerciseOneNumbers)

  println(adventOfCode.multiplyNumbersInList(findNumbersSumTo2020))

  val collectionsOfNumbersThatAddToRemainder: Seq[Seq[Int]] = adventOfCode.remainders.map(i => adventOfCode.remainderMinusOriginalNumbersCollectPositive(i))
  val filterCloudsBasedOnOriginals: Seq[Seq[Int]] = collectionsOfNumbersThatAddToRemainder.map(xs => xs.flatMap(x => exerciseOneNumbers.filter(_ == x)))
  val filterEmptyClouds: Seq[Seq[Int]] = filterCloudsBasedOnOriginals.filterNot(_.isEmpty)
  // these numbers add to 2020 - x, to determine the numbers in the original list that add to x
  // filterCloudsBasedOnOriginals gets back the two numbers which add to the remainder that are also present in the original sequence together you can deduce
  // the 3 numbers which add to 2020, so go ahead and multiply them all to get exercise 1 part 2 answer
  println(filterCloudsBasedOnOriginals)
  println(filterEmptyClouds)
}
