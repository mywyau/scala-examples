package rockTheJVM.dataManipulation

import cats.Id

object Readers {

  /*
  - confirguration file => initial data structure
  - a DB layer
  - an HTTP layer
  - a business logic layer
   */

  // Reader is a data processing pattern

  case class Configuration(dbUserName: String, dbPass: String, host: String, post: Int, nThreads: Int, emailReplyTo: String)

  case class DbConnection(userName: String, password: String) {

    def getOrderStatus(orderId: Long): String = "dispatched" // select * from the db table and return the status of the orderId

    def getLastOrderId(userName: String): Long = 542643 // in reality you will get the last user order Id - this is just a dummy value use your imagination
  }

  case class HttpService(host: String, port: Int) {
    def start(): Unit = println("Server started") // this would start the actual server
  }

  val config = Configuration("daniel", "rock the jvm1!", "localhost", 1234, 8, "daniel@rockTheJVM.com")

  import cats.data.Reader

  val dbReader: Reader[Configuration, DbConnection] = Reader { conf => DbConnection(conf.dbUserName, conf.dbPass) }

  val dbConn: Id[DbConnection] = dbReader.run(config) //injection but via functions

  //Reader[I, O]

  val danielOrderStatusReader: Reader[Configuration, String] = dbReader.map(dbcon => dbcon.getOrderStatus(55)) // we can manipulate the output of the Reader via .map
  val danielOrderStatus: Id[String] = danielOrderStatusReader.run(config) // this lets us get different information form the started data structure which is the Configuration type

  def getLastOrderStatus(username: String) = {
    val usersLastOrderIdReader =
      dbReader
        .map(dbConn => dbConn.getLastOrderId(username))
        .flatMap(lastOrderId => dbReader.map(_.getOrderStatus(lastOrderId)))

    val usersOrderFor =
      for {
        lastOrderId <- dbReader.map(dbConn => dbConn.getLastOrderId(username))
        orderStatus <- dbReader.map(dbConn => dbConn.getOrderStatus(lastOrderId))
      } yield orderStatus


    //    usersLastOrderIdReader.run(config)   //map, flatMap chaining
    usersOrderFor.run(config) // for comprehension version
  }

  case class EmailService(emailReplyTo: String) {

    def sendEmail(address: String, contents: String) = s"From: $emailReplyTo, to:$address >>> $contents"
  }


  def emailUser(userName: String, userEmail: String) = {
    val emailServiceReader: Reader[Configuration, EmailService] = Reader { conf => EmailService(conf.emailReplyTo) }
    val emailReader: Reader[Configuration, String] =
      for {
        lastOrderId <- dbReader.map(dbConn => dbConn.getLastOrderId(userName))
        orderStatus <- dbReader.map(dbConn => dbConn.getOrderStatus(lastOrderId))
        emailService <- emailServiceReader
      } yield emailService.sendEmail(userEmail, s"Your last order has status: $orderStatus")

    emailReader.run(config)
  }

  // Dependency Injection implemented via Reader using pure functions

  def main(args: Array[String]): Unit = {

    println(emailUser("daniel", "daniel@rtjvm.com"))

  }
}
