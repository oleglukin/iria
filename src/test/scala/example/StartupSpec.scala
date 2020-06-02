package example

import org.scalatest._

class Startup extends FlatSpec with Matchers {
  "The Hello object" should "say hello" in { // TODO update spec
    Startup.greeting shouldEqual "hello"
  }
}
