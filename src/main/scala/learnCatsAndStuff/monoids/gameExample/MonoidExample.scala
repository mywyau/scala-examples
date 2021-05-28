package learnCatsAndStuff.monoids.gameExample

import cats.Monoid
import cats.syntax.semigroup._

sealed trait Monster

case class Slime(level: Int,
                 element: Element,
                 hp: BigDecimal,
                 att: BigDecimal,
                 defence: BigDecimal,
                 speed: BigDecimal) extends Monster

case class SlimeKnight(level: Int,
                       element: Element,
                       hp: BigDecimal,
                       att: BigDecimal,
                       defence: BigDecimal,
                       speed: BigDecimal) extends Monster

case class ElementalWarrior(level: Int,
                            element: Element,
                            hp: BigDecimal,
                            att: BigDecimal,
                            defence: BigDecimal,
                            speed: BigDecimal) extends Monster

object MonoidExample {

  /* Game setting

  - some kinda complicated Slime stat combination logic

   - I wish to combine some Slimes for my monster as an ability.
   - Slimes always inherit the stronger Slime's Element
   - Slimes are pretty slow but as they grow stronger they do become faster. In the case of Slime Fusion the minimum speed a fused Slime can have is 20
   - Multiple Slimes can combine together at once there is no limit of slimes that can fuse at once (strength in numbers),
     Obviously the stronger Slime that fuses first is the host and overwhelms the others in fusion.
   - TODO: The Stats are still all combined for multiple Slimes, however after 5 Slimes combine,
      then additional Slimes only contribute 10% Stat growth add counter to Slime model and update calculation
   - there can also be status caps to prevent unfair defence. The defence of a Slime cannot be more than 10x the level

   */

  implicit val slimeMonoid: Monoid[Slime] = new Monoid[Slime] {

    /*
       you should probably break up your logic and stats into different typeclasses and stuff but for simple dumb example for now
       this is my monoid typeclass instance for Slime I can use Semigroup syntax to combine Slimes in the future
       this is definitely not a good way to write out a game obviously

    */

    override def combine(firstSlime: Slime, secondSlime: Slime): Slime = {

      val totalStats: Slime => BigDecimal = (s: Slime) =>
        (s.hp + s.att + s.defence + s.speed) * ((100 + s.level) / 100)

      val firstSlimeTotalStats = totalStats(firstSlime)
      val secondSlimeTotalStats = totalStats(secondSlime)

      println(s"first slime level: ${firstSlime.level} \n - total stats: $firstSlimeTotalStats, element: ${firstSlime.element}")
      println(s"second slime level: ${secondSlime.level} \n - total stats: $secondSlimeTotalStats, element: ${secondSlime.element}\n")

      val elementDecider = (firstSlimeTotalStats, secondSlimeTotalStats) match {
        case _ if firstSlime.element == Neutral =>
          println(s"First slime was Neutral, so final Slime element: ${secondSlime.element}")
          secondSlime.element
        case _ if secondSlime.element == Neutral =>
          println(s"Second slime was Neutral, so final Slime element: ${firstSlime.element}")
          firstSlime.element
        case (totalStatOne, totalStatTwo) if totalStatOne > totalStatTwo =>
          println(s"slime element set: ${firstSlime.element}")
          firstSlime.element
        case _ =>
          println(s"slime element set: ${secondSlime.element}")
          secondSlime.element
      }

      //      if (firstSlimeTotalStats > secondSlimeTotalStats) {
      //        println(s"slime element set: ${firstSlime.element}")
      //        firstSlime.element
      //      } else {
      //        println(s"slime element set: ${secondSlime.element}")
      //        secondSlime.element
      //      }

      val levelFormula: Int = (firstSlime.element, secondSlime.element) match {
        case (Neutral, _) => secondSlime.level
        case (_, Neutral) => firstSlime.level
        case _ =>
          math.ceil(
            ((firstSlime.level + secondSlime.level) * 1.3) / 2
          ).toInt
      }

      val defenceFormula = (firstSlime.defence + secondSlime.defence) * 1.1
      val defenceCap: BigDecimal = levelFormula * 5
      val totalDefenceStat: BigDecimal = if (defenceFormula < defenceCap) {
        defenceFormula
      } else {
        println("Hit max defence for level")
        defenceCap
      }

      val speedCalc: BigDecimal =
        if (firstSlime.speed + secondSlime.speed < 20) {
          println(s"the speed was lower than the base speed of fusion slime, speed was set to 20 instead of: ${firstSlime.speed + secondSlime.speed}\n")
          20.0
        } else {
          println(s"fused slime speed is : ${firstSlime.speed + secondSlime.speed}, which is greater than minimum," +
            s" speed set to ${firstSlime.speed + secondSlime.speed}\n")
          firstSlime.speed + secondSlime.speed
        }

      val finalFusedSlime =
        Slime(
          level = levelFormula,
          element = elementDecider,
          hp = firstSlime.hp + secondSlime.hp * 1.1 / 2,
          att = firstSlime.att + secondSlime.att * 1.1 / 2,
          defence = totalDefenceStat,
          speed = speedCalc
        )

      println(
        s"Final Slime Lv: ${finalFusedSlime.level}\n" +
          s"Final Slime Element: ${finalFusedSlime.element}\n" +
          "-" * 20 + "\n"
      )

      finalFusedSlime
    }

    override def empty: Slime =
      Slime(
        level = 0,
        element = Neutral,
        hp = 0,
        att = 0,
        defence = 0,
        speed = 0
      )
  }

  implicit val slimeKnightMonoid: Monoid[SlimeKnight] = new Monoid[SlimeKnight] {

    // you should probably break up your logic and stats into different typeclasses and stuff but for simple dumb example for now
    // this is my monoid typeclass instance for Slime I can use Semigroup syntax to combine Slimes in the future
    // this is definitely not a good way to write out a game obviously

    override def combine(firstSlime: SlimeKnight, secondSlime: SlimeKnight): SlimeKnight = {

      val totalStats: SlimeKnight => BigDecimal = (s: SlimeKnight) =>
        (s.hp + s.att + s.defence + s.speed) * ((100 + s.level) / 100)

      val firstSlimeTotalStats = totalStats(firstSlime)
      val secondSlimeTotalStats = totalStats(secondSlime)

      println(s"First SlimeKnight level: ${firstSlime.level} \n - total stats: $firstSlimeTotalStats, element: ${firstSlime.element}")
      println(s"Second SlimeKnight level: ${secondSlime.level} \n - total stats: $secondSlimeTotalStats, element: ${secondSlime.element}\n")

      val elementDecider = (firstSlimeTotalStats, secondSlimeTotalStats) match {
        case _ if firstSlime.element == Neutral =>
          println(s"First SlimeKnight was Neutral, so final Slime element: ${secondSlime.element}")
          secondSlime.element
        case _ if secondSlime.element == Neutral =>
          println(s"Second SlimeKnight was Neutral, so final Slime element: ${firstSlime.element}")
          firstSlime.element
        case (totalStatOne, totalStatTwo) if totalStatOne > totalStatTwo =>
          println(s"Slime element set: ${firstSlime.element}")
          firstSlime.element
        case _ =>
          println(s"Slime element set: ${secondSlime.element}")
          secondSlime.element
      }

      //      if (firstSlimeTotalStats > secondSlimeTotalStats) {  old code for level
      //        println(s"slime element set: ${firstSlime.element}")
      //        firstSlime.element
      //      } else {
      //        println(s"slime element set: ${secondSlime.element}")
      //        secondSlime.element
      //      }

      val levelFormula: Int = (firstSlime.element, secondSlime.element) match {
        case (Neutral, _) => secondSlime.level
        case (_, Neutral) => firstSlime.level
        case _ =>
          math.ceil(
            ((firstSlime.level + secondSlime.level) * 1.2) / 2
          ).toInt
      }

      val defenceFormula = (firstSlime.defence + secondSlime.defence) * 1.2
      val defenceCap: BigDecimal = levelFormula * 10
      val totalDefenceStat: BigDecimal = if (defenceFormula < defenceCap) {
        println(
          s"\nMax defence stat for level: $levelFormula is $defenceCap" +
            s"\nCalculated Defence: $defenceFormula" +
            s"\nHas not yet hit cap for Monster Level, set defence: $defenceFormula"
        )
        defenceFormula
      } else {
        println(
          s"\nMax defence stat for level: $levelFormula is $defenceCap" +
            s"\nCalculated Defence: $defenceFormula" +
            s"\nHit the defence stat cap, set defence: $defenceCap"
        )
        defenceCap
      }

      val speedCalc: BigDecimal =
        if (firstSlime.speed + secondSlime.speed < 20) {
          println(s"\nThe speed was lower than the base speed of fusion SlimeKnight, speed was set to 40 instead of: ${firstSlime.speed + secondSlime.speed}\n")
          40.0
        } else {
          println(s"Fused SlimeKnight speed is : ${firstSlime.speed + secondSlime.speed}, which is greater than minimum," +
            s" speed set to ${firstSlime.speed + secondSlime.speed}\n")
          firstSlime.speed + secondSlime.speed
        }

      val finalFusedSlime =
        SlimeKnight(
          level = levelFormula,
          element = elementDecider,
          hp = firstSlime.hp + secondSlime.hp * 1.3 / 2,
          att = firstSlime.att + secondSlime.att * 1.3 / 2,
          defence = totalDefenceStat,
          speed = speedCalc
        )

      println(
        s"Final Slime Lv: ${finalFusedSlime.level}\n" +
          s"Final Slime Element: ${finalFusedSlime.element}\n" +
          "-" * 20 + "\n"
      )

      finalFusedSlime
    }

    override def empty: SlimeKnight =
      SlimeKnight(
        level = 0,
        element = Neutral,
        hp = 0,
        att = 0,
        defence = 0,
        speed = 0
      )


  }

  //  implicit val intMonoid : Monoid[Int] = new Monoid[Int] {
  //
  //    override def combine(x: Int, y: Int): Int = (x + y) * 3
  //
  //    override def empty: Int = 0
  //  }


  //TODO: Make a nicer format to view your slimes or just comment out fusion slimes and respective printlns

  import SlimeBank._
  //    val fireAndIceSlimeFusion = fireSlime |+| iceSlime
  //    val iceAndThunderSlimeFusion = thunderSlime |+| iceSlime

  //    val multipleSlimes: Slime = thunderSlime |+| iceSlime |+| fireSlime |+| iceSlime |+| slimeMonoid.empty // |+| thunderSlime
  //    val multipleSlimesAddNeutralSlime: Slime = multipleSlimes |+| slimeMonoid.empty

}

object MonoidExampleRunner extends App {

  import MonoidExample._
  import SlimeBank._

  //  println(fireAndIceSlimeFusion)
  //  println(iceAndThunderSlimeFusion)
  //
    val multipleSlimes: Slime = thunderSlime |+| iceSlime |+| fireSlime |+| iceSlime |+| thunderSlime
    val multipleSlimesAddNeutralSlime: Slime = multipleSlimes |+| slimeMonoid.empty
    println(multipleSlimes)
  //
  //  val multipleSlimeKnights: SlimeKnight = thunderSlimeKnight |+| iceSlimeKnight |+| fireSlimeKnight |+| iceSlimeKnight |+| thunderSlimeKnight
  //  val multipleSlimeAddNeutralSlime: SlimeKnight = multipleSlimeKnights |+| slimeKnightMonoid.empty
  //
  //  println(multipleSlimeKnights)
  //  println(multipleSlimeAddNeutralSlime)
  //
  //  println(3 |+| 3)


}