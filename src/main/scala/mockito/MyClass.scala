package mockito

class MyClass() {

  def myMethod(number: Int) = if (number > 5) "foo" else "mikey"

  def anotherMethod(param: String, i: Int): String =
    myMethod(i) match {
      case "foo" => param + " foo"
      case _ => "not foo"
    }


  def bar = "not mocked"

  def baz(param: String) = s"$param not mocked"
}

class Foo {

  def iHaveByNameArgs(normal: String, byName: => String, byName2: => String): String = s"$normal - $byName - $byName2"

  def iStartWithByNameArgs(byName: => String, normal: String): String = s"$normal - $byName"
}

trait MyTrait {

  def traitMethod = "trait"
}

class TempScalaService() {

  def login(userName: String, password: String): Boolean = {
    if (userName.equals("root") && password.equals("admin123")) {
      true
    } else {
      false
    }
  }
}