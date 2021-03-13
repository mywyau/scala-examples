package scala.coursera.week2

import scala.annotation.tailrec

class FunctionalSets {

  type FunSet = Int => Boolean //alias

  def contains(s: FunSet, elem: Int): Boolean = s(elem)

  def singletonSet(elem: Int): FunSet = {
    //elem == (_:Int)  alternative
    x: Int => elem == x: Boolean
  }

  def union(s: FunSet, t: FunSet): FunSet = {

    // If the given element is in the union of two sets it must be in either 's' or 't' i.e set1 or set2

    val typeCheckUnion: Int => Boolean = (i: Int) => s(i) | t(i) // did define as anonymous function first, i: Int => s(i) | t(i)
    typeCheckUnion
  }

  def intersect(s: FunSet, t: FunSet): FunSet = {

    // If the given element is in the intersect of two sets it must be in both 's' or 't' i.e set1 or set2

    i: Int => s(i) && t(i)
  }

  /**
   * Returns the difference of the two given sets,
   * the set of all elements of `s` that are not in `t`.
   */
//  def diff(s: Set, t: Set): Set = (e: Int) => s(e) && !t(e)


  def diff(s: FunSet, t: FunSet): FunSet = {

    // If the given element is in 's' and not 't' then it must be s(i) but minus the intersect of 's' and 't'

    i: Int => s(i) && !intersect(s, t)(i) //i can be curried which is a little confusing in type aliased method form
  }


  def filter(s: FunSet, p: Int => Boolean): FunSet = {

    (i:Int) => s(i) && p(i)
  }

//  val s: Int => Boolean = (a: Int) => a > 1000 || a < -1000
//  val p: Int => Boolean = (a: Int) => a <= 1000 && a >= -1000
//
//  def forall(s: FunSet, p: Int => Boolean): Boolean = {
//    @tailrec
//    def iter(a: Int): Boolean = {
//      if (s(a)) false
//      else if (p(a)) true
//      else iter(a)
//    }
//
//    iter(???)
//  }





}
