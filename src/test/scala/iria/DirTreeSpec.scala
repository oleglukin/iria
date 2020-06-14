package iria

import iria.model.{DirTree, DirItem, DirItemStatus}
import java.time.LocalDateTime
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DirTreeSpec extends AnyFlatSpec with Matchers {

  def now = LocalDateTime.now
  
  def treeWithOneChildItem = new DirTree(
    new DirItem("/", "dir1", 2, now, false, None),
    Seq(
      new DirTree(
        new DirItem("/", "file1", 2, now, true, None),
        Seq()
      )
    )
  )

  "DirTree.addStatus method" should "add status to its root node" in {
    val status = DirItemStatus.Missing
    val tree = treeWithOneChildItem.addStatus(status)
    tree.node.status should be (Some(status))
  }

  it should "add status to its children" in {
    val status = DirItemStatus.Missing
    val tree = treeWithOneChildItem.addStatus(status)
    tree.children.length should be > 0
    tree.children(0).node.status should be (Some(status))
  }
}