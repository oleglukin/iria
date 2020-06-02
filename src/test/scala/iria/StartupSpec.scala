package eventsource

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class StartupSpec extends AnyFlatSpec with Matchers {
  "The EventSourceApp object" should "return result" in {
    Startup.func shouldEqual "result"
  }
}