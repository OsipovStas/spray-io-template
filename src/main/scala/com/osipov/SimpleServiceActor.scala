package com.osipov

/**
 * @author stas
 * @since  15.11.15
 */

import akka.actor.Actor
import com.osipov.PersonJsonProtocol._
import spray.http.MediaTypes._
import spray.http._
import spray.httpx.SprayJsonSupport._
import spray.routing._

class SimpleServiceActor extends Actor with HttpService {

  // handles the api path, we could also define these in separate files
  // this path respons to get queries, and make a selection on the
  // media-type.
  val aSimpleRoute = {
    path("path1") {
      get {
        // Get the value of the content-header. Spray
        // provides multiple ways to do this.
        val directive: Directive1[ContentType] = headerValue({
          case x@HttpHeaders.`Content-Type`(value) => Some(value)
          case default => None
        })
        val function: (ContentType) => Route = {

          // if we have this contentype we create a custom response
          case ContentType(MediaType("application/vnd.type.a"), _) => {
            respondWithMediaType(`application/json`) {
              complete {
                Person("Bob", "Type A", System.currentTimeMillis());
              }
            }
          }

          // if we have another content-type we return a different type.
          case ContentType(MediaType("application/vnd.type.b"), _) => {
            respondWithMediaType(`application/json`) {
              complete {
                Person("Bob", "Type B", System.currentTimeMillis());
              }
            }
          }

          // if content-types do not match, return an error code
          case default => {
            complete {
              HttpResponse(406);
            }
          }
        }
        directive(function)
      }
    }
  }
  // handles the other path, we could also define these in separate files
  // This is just a simple route to explain the concept
  val anotherRoute = {
    path("path2") {
      get {
        // respond with text/html.
        respondWithMediaType(`text/html`) {
          complete {
            // respond with a set of HTML elements
            <html>
              <body>
                <h1>Path 2</h1>
              </body>
            </html>
          }
        }
      }
    }
  }

  // required as implicit value for the HttpService
  def actorRefFactory = context

  // we don't create a receive function ourselve, but use
  // the runRoute function from the HttpService to create
  // one for us, based on the supplied routes.
  def receive = runRoute(aSimpleRoute ~ anotherRoute)

}

