package iria

import iria.model.{DirItem, DirItemStatus}
import java.time.LocalDateTime
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DirItemSpec extends AnyFlatSpec with Matchers {
  "A DirItem addStatus function" should "return a copy with provided status" in {
    val item = new DirItem("/", "dir1", 2, LocalDateTime.now, false, None)
    val status = DirItemStatus.New
    val copy = item addStatus status

    copy.name shouldEqual item.name
    copy.status shouldEqual Some(DirItemStatus.New)
  }
}