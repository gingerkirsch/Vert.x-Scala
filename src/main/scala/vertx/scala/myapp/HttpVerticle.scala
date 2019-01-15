package vertx.scala.myapp

import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.eventbus.Message
import io.vertx.scala.ext.web.{Router, RoutingContext}

import scala.concurrent.Future
import scala.util.Try

class HttpVerticle extends ScalaVerticle {

  override def startFuture(): Future[_] = {

    vertx.deployVerticle(ScalaVerticle.nameForVerticle[BusVerticle])

    //Create a router to answer GET-requests to "/hello" with "world"
    val router = Router.router(vertx)
    val route = router
      .get("/hello")
      .handler(sendHelloToBus)

    vertx.deployVerticle(new BusVerticle)

    vertx
      .createHttpServer()
      .requestHandler(router.accept _)
      .listenFuture(8666, "0.0.0.0")
  }

  def  sendHelloToBus(rc: RoutingContext ) {
    println("Send Hello to Bus method")

    vertx.eventBus().sendFuture("testAddress", "Hello").onComplete(event => replyBusHandler(event))

    rc.response().end("Request response")
  }

  def replyBusHandler(param: Try[Message[Nothing]]) {
    if (param.isSuccess) {
      println(param.get.body())
    }
  }
}