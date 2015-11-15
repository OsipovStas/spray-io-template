package com.osipov

/**
 * @author stas
 * @since  15.11.15
 */

import spray.json.DefaultJsonProtocol

object PersonJsonProtocol extends DefaultJsonProtocol {
  implicit val personFormat = jsonFormat3(Person)
}

case class Person(name: String, fistName: String, age: Long)
