package iria

import iria.logic.TreeSort
import iria.model.{DirTree, DirItem}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import java.time.LocalDateTime

class TreeSortSpec extends AnyFlatSpec with Matchers {
  // Check sortTrees function

  def now = LocalDateTime.now
  def rootNode = new DirItem("/", "dir1", 2, now, false, None)
  def emptyDir = new DirTree(rootNode, Seq())

  "An empty DirTree" should "remain itself after soriting" in {
    val sorted = TreeSort.sortTrees(Seq(emptyDir), true)(0)
    sorted.node.name shouldEqual emptyDir.node.name
    sorted.children.length should be (0)
  }

  def dirItemFile = new DirItem("/", "file1", 1, now, true, None)
  def dirItemDir = new DirItem("/", "dir1", 2, now, false, None)

  def treeWithSubdirAndFile = new DirTree(
    rootNode,
    Seq(
      new DirTree(dirItemDir, Seq()),
      new DirTree(dirItemFile, Seq())
    )
  )

  "TreeSort.sortTrees function has filesFirst parameter. It" should "put files first if true" in {
    val sorted = TreeSort.sortTrees(Seq(treeWithSubdirAndFile), true)(0)
    sorted.children.length should be >= 2
    sorted.children(0).node.isFile should be (true)
    sorted.children(1).node.isFile should be (false)
  }
  
  it should "put directories first if filesFirst parameter is false" in {
    val sorted = TreeSort.sortTrees(Seq(treeWithSubdirAndFile), false)(0)
    sorted.children.length should be >= 2
    sorted.children(0).node.isFile should be (false)
    sorted.children(1).node.isFile should be (true)
  }

  def treeWithMultipleFiles = new DirTree(
    rootNode,
    Seq(
      new DirTree(new DirItem("/", "fileXYZ", 1, now, true, None), Seq()),
      new DirTree(new DirItem("/", "fileABC", 1, now, true, None), Seq())
    )
  )

  "A DirTree with multiple items of same type (file or dir) when sorted" should "have those items sorted alphanumerically" in {
    val unsorted = treeWithMultipleFiles.children
    unsorted(0).node.name should be ("fileXYZ")
    unsorted(1).node.name should be ("fileABC")

    val sorted = TreeSort.sortTrees(Seq(treeWithMultipleFiles), true)(0).children
    sorted(0).node.name should be ("fileABC")
    sorted(1).node.name should be ("fileXYZ")
  }
}