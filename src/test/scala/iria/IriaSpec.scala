package eventsource

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class IriaSpec extends AnyFlatSpec with Matchers {
  "The EventSourceApp object" should "return result" in {
    Iria.func shouldEqual "result"
  }
}