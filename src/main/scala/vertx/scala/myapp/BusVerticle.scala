package vertx.scala.myapp

import io.vertx.lang.scala.ScalaVerticle
import io.vertx.scala.core.eventbus.Message

import scala.concurrent.Future

class BusVerticle extends ScalaVerticle {

  override def startFuture(): Future[_] = {

    vertx
      .eventBus()
      .consumer[String]("testAddress")
      .handler(returnMessage(_))
      .completionFuture()
  }

  def returnMessage(mess: Message[String]) {
    println("Return message method")
    mess.reply("Received" + mess.body())
  }
}
