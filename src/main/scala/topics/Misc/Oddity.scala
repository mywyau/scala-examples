package topics.Misc

class Oddity {

  def add1(x: {def get: Int}): Int = 1 + x.get

  final case class Wrapper(i: Int) {
    def get: Int = i
  }

  final case class Wrapper2(i: Int) {
    def set: Int = i
  }

  add1(Wrapper(1)) //has a get method
  //  add1(Wrapper2(1))   //does not have a get method

  //It is, however, possible to express the notion of has a get: Int method as a type class:

  trait HasGet[A] {
    def get(a: A): Int
  }

  // add1 now works with any type on which value you can call get
  def add1[A](a: A)(implicit hga: HasGet[A]): Int = hga.get(a) + 1

  implicit val wrap: HasGet[Wrapper] = new HasGet[Wrapper] {
    def get(a: Wrapper) = a.i
  }

  add1(Wrapper(1))
  // res1: Int = 2
}
