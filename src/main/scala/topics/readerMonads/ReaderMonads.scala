package topics.readerMonads

import cats.Id
import cats.data.{Kleisli, Reader}


object ReaderMonads {

  // Reader monads appear to be a replacement for dependency injection, it appears you can just use or replace it with just Kleisli

  val upper: Reader[String, String] = Reader((text: String) => text.toUpperCase)
  val greet: Reader[String, String] = Reader((name: String) => s"Hello $name")

  val comb1 = upper.compose(greet)
  val comb2 = upper.andThen(greet)

  val result = comb1.run("Bob")
  val result2 = comb2.run("Bob")

  /*
    Dependency Injection

      Consider the two services below. The user can only register in a course if he is authorised.
      So the CourseService is dependent on the result of the AuthService
  */

  case class Course(desc: String, code: String)

  class AuthService {

    def isAuthorised(userName: String): Boolean = {
      userName.startsWith("J")
    }
  }

  class CourseService {

    def register(course: Course,
                 isAuthorised: Boolean,
                 name: String) = {
      if (isAuthorised) // this predicate here is derived from the AuthService so we say the CourseService is dependant on the AuthService
        s"User $name registered for the course: ${course.code}"
      else
        s"User: $name is not authorised to register for course: ${course.code}"
    }
  }

  /*
    We will create an environment via a class which will represent this environment and call it Course Manager
    Notice houw
  */

  case class CourseManager(course: Course,
                           userName: String,
                           authService: AuthService,
                           courseService: CourseService)


  /*
   When the application is run the CourseManager will have all the services needed to carry out the business transaction.
      Let’s create the Reader monad which allows us to write business methods not tied to the services
  */

  def isAuthorised: Reader[CourseManager, Boolean] = Reader[CourseManager, Boolean] {
    courseManager: CourseManager => courseManager.authService.isAuthorised(courseManager.userName) // notice we have rewritten the above isAuthorised method into a function contained within the Reader Monad
  }

  def register(isFull: Boolean) =
    Reader[CourseManager, String] {
      courseManager: CourseManager =>
        courseManager.courseService
          .register(
            courseManager.course,
            isFull,
            courseManager.userName
          )
    }

  def isAuthorisedK: Reader[CourseManager, Boolean] = Kleisli[Id, CourseManager, Boolean] {
    courseManager: CourseManager => courseManager.authService.isAuthorised(courseManager.userName) // notice we have rewritten the above isAuthorised method into a function contained within the Reader Monad
  }

  def registerK(isFull: Boolean): Kleisli[Id, CourseManager, String] = Kleisli[Id, CourseManager, String] {
    courseManager: CourseManager =>
      courseManager.courseService
        .register(
          courseManager.course,
          isFull,
          courseManager.userName
        )
  }

  def isAuthorisedK2: Reader[CourseManager, Boolean] = Reader[CourseManager, Boolean] {
    courseManager: CourseManager => courseManager.authService.isAuthorised(courseManager.userName) // notice we have rewritten the above isAuthorised method into a function contained within the Reader Monad
  }

  def registerK2(isFull: Boolean): Kleisli[Id, CourseManager, String] = Reader[CourseManager, String] {
    courseManager: CourseManager =>
      courseManager.courseService
        .register(
          courseManager.course,
          isFull,
          courseManager.userName
        )
  }

  val authResultK =
    for {
      authorised <- isAuthorisedK
      response <- registerK(authorised)
    } yield response


  // Now since in both functions the CourseManager case class type is present in the return type we can combine these in a for comprehension. they are both monads after all

  val authResult =
    for {
      authorised <- isAuthorised
      response <- register(authorised)
    } yield response

  val desiredCourse = Course(desc = "Computer Science", code = "CS01")
  val courseManager = CourseManager(desiredCourse, "Jonathan", new AuthService, new CourseService)

  val executeService = authResult.run(courseManager)
  val executeServiceK = authResultK.run(courseManager)

}


object ReaderRunner extends App {

  import topics.readerMonads.ReaderMonads._

  println(result) // prints HELLO BOB
  println(result2) // prints Hello Bob

  println(executeService) // prints Hello Bob


}