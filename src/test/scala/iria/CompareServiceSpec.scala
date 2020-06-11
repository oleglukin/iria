package iria

import iria.CompareService
import iria.model.{DirItem, DirTree, DirItemStatus}
import java.time.LocalDateTime
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CompareServiceSpec extends AnyFlatSpec with Matchers {
  "A DirTree when compared to itself" should "return a pair of its copies with status 'Same'" in {
    val node = new DirItem("/", "dir1", 2, LocalDateTime.now, false, None)
    val tree = new DirTree(node, Seq())
    val (left, right) = CompareService.compare(tree, tree)
    left.node.status shouldEqual DirItemStatus.Same
    right.node.status shouldEqual DirItemStatus.Same
  }
}