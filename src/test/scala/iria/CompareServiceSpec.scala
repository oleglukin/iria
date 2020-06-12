package iria

import iria.CompareService
import iria.model.{DirItem, DirTree, DirItemStatus}
import java.time.LocalDateTime
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CompareServiceSpec extends AnyFlatSpec with Matchers {

  def rootNode = new DirItem("/", "dir1", 2, LocalDateTime.now, false, None)
  def emptyDir = new DirTree(rootNode, Seq())

  "A DirTree when compared to itself" should "return a pair of its copies" in {
    val tree = emptyDir
    val (left, right) = CompareService.compare(tree, tree)
    
    left.node.name shouldEqual tree.node.name
    right.node.name shouldEqual tree.node.name
    
  }

  it should "return those copies with status 'Same'" in {
    val tree = emptyDir
    val (left, right) = CompareService.compare(tree, tree)

    left.node.status shouldEqual Some(DirItemStatus.Same)
    right.node.status shouldEqual Some(DirItemStatus.Same)
  }


  def treeWithOneFile = {
    val file =  new DirItem("/dir1", "file1", 462, LocalDateTime.now, true, None)
    val fileDirTree = new DirTree(file, Seq())
    new DirTree(rootNode, Seq(fileDirTree))
  }

  "A tree with a missing file" should "get this file marked as missing" in {
    val (left, right) = CompareService.compare(emptyDir, treeWithOneFile)
    left.children.length should be > 0
    left.children(0).node.status shouldEqual Some(DirItemStatus.Missing)
  }

  it should "also mark this file in the other tree as new" in {
    val (left, right) = CompareService.compare(emptyDir, treeWithOneFile)
    right.children.length should be > 0
    right.children(0).node.status shouldEqual Some(DirItemStatus.New)
  }
}