package com.osipov

/**
 * @author stas
 * @since  15.11.15
 */

import akka.actor.{ActorRef, ActorSystem, Props}
import spray.servlet.WebBoot

class SprayServletApi extends WebBoot with SprayBootApi {
  override def serviceActor: ActorRef = service
}


trait SprayBootApi {

  implicit val system = ActorSystem("simple-system")
  val service = system.actorOf(Props[SimpleServiceActor], "simple-rest-service")

}

