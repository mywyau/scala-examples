package rockTheJVM.abstractMath

object UsingMonads {

  import cats.Monad
  import cats.instances.list._

  val monadList = Monad[List]

  val aSimpleList = monadList.pure(2)
  val anExtendedList = monadList.flatMap(aSimpleList)(x => List(x, x + 1))

  //applicable to Option, Try, Future

  //Either

  val anEitherR: Either[String, Int] = Right(42)
  val anEitherL: Either[String, Int] = Left("kill me")

  import cats.instances.either._

  val loadingMonad = Monad[LoadingOr]
  val anEither = loadingMonad.pure(45)
  val aChangedLoading = loadingMonad.flatMap(anEither)(n => if (n % 2 == 0) Right(n + 1) else Left("Loading meaning of life..."))

  case class OrderStatus(orderId: Long, status: String)

  def getOrderStatus(orderId: Long): LoadingOr[OrderStatus] =
    Right(OrderStatus(orderId, "Ready to ship"))

  def trackLocation(orderStatus: OrderStatus): LoadingOr[String] =
    if (orderStatus.orderId > 1000) Left("Not available yet, refreshing data...") else Right("Amsterdam, NL")

  val orderId = 457L

  val orderLocation = loadingMonad.flatMap(getOrderStatus(orderId))(orderStatus => trackLocation(orderStatus))

  // use extension methods

  val orderLocationBetter: LoadingOr[String] = getOrderStatus(orderId).flatMap(orderStatus => trackLocation(orderStatus))

  val orderLocationFor: LoadingOr[String] =
    for {
      orderStatus <- getOrderStatus(orderId)
      location <- trackLocation(orderStatus)
    } yield location

  type LoadingOr[T] = Either[String, T] //common pattern
  type ErrorOr[T] = Either[Throwable, T] //common pattern

  //TODO: the service layer API of a web app

  case class Connection(host: String, port: String)

  val config =
    Map(
      "host" -> "localhost",
      "port" -> "4040"
    )

  trait HttpService[M[_]] {

    def getConnection(cfg: Map[String, String]): M[Connection]

    def issueRequest(connection: Connection, payLoad: String): M[String]
  }

  import cats.syntax.flatMap._   // these imports are needed to make the generic .getResponse() work
  import cats.syntax.functor._   // once you get it working with the implicit then just replace the implicit with a context bound on the type M[_]

  def getResponse[M[_]: Monad](service: HttpService[M], payLoad: String): M[String] = {
    for {
      conn <- service.getConnection(config)
      response <- service.issueRequest(conn, payLoad)
    } yield response
  }

  object OptionHttpService extends HttpService[Option] {

    override def getConnection(cfg: Map[String, String]): Option[Connection] =
      for {
        h <- cfg.get("host")
        p <- cfg.get("port")
      } yield Connection(h, p)

    override def issueRequest(connection: Connection, payLoad: String): Option[String] =
      if (payLoad.length >= 20) {
        println(payLoad)
        None
      } else Some(s"Request $payLoad has been accepted")
  }

  object AggressiveHttpService extends HttpService[ErrorOr] {

    override def getConnection(cfg: Map[String, String]): ErrorOr[Connection] =
      if (!cfg.contains("host") || !cfg.contains("port")) Left(new RuntimeException("Connection could not be established: invalid configuration"))
      else Right(Connection(cfg("host"), cfg("port")))

    override def issueRequest(connection: Connection, payLoad: String): ErrorOr[String] =
      if (payLoad.length >= 20) {
        println(payLoad)
        Left(new RuntimeException("Payload >= 20"))
      } else Right(s"Request $payLoad has been accepted")
  }

  def main(args: Array[String]): Unit = {

    val responseOption: Option[String] =
      for {
        conn <- OptionHttpService.getConnection(config)
        response <- OptionHttpService.issueRequest(conn, "Hello, HTTP service")
      } yield response

    println(responseOption)

    val aggressiveResponse: Either[Throwable, String] =
      for {
        conn <- AggressiveHttpService.getConnection(config)
        response <- AggressiveHttpService.issueRequest(conn, "Hello, HTTP service")
      } yield response

    println(aggressiveResponse)
  }
}
