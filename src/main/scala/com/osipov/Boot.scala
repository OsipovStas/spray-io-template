package com.osipov

/**
 * @author stas
 * @since  15.11.15
 */

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import spray.can.Http

import scala.concurrent.duration._

object Boot extends App {

  implicit val system = ActorSystem("simple-system")
  val service = system.actorOf(Props[SimpleServiceActor], "simple-rest-service")

  // IO requires an implicit ActorSystem, and ? requires an implicit timeout
  // Bind HTTP to the specified service.
  implicit val timeout = Timeout(5.seconds)
  IO(Http) ? Http.Bind(service, interface = "localhost", port = 8185)
}
