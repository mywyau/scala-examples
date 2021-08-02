//package mockito
//
//import org.scalatest.{Matchers, WordSpec}
//
//class MyClassSpec extends WordSpec with Matchers with MockitoSugar {
//
//  val mockMyClass: MyClass = mock[MyClass]
//
//  val aMock: Foo = mock[Foo]
//
//  val myClass = new MyClass()
//
//  object TempScalaService {
//    private val service = TempScalaService()
//
//    def apply(): TempScalaService = service
//  }
//
//  val service = mock[TempScalaService]
//
//  when(service.login("user", "testuser")).thenReturn(true)
//
//  "MyClass mockito example" should {
//
//    "return foo" in {
//
//      when(mockMyClass.myMethod(1)) thenReturn "mikey"
//
//      val result = myClass.anotherMethod("myParam", 1)
//
//      result shouldBe (5)
//    }
//  }
//
//}
