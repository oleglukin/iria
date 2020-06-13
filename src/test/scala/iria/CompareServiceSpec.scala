package iria

import iria.CompareService
import iria.model.{DirItem, DirTree, DirItemStatus}
import java.time.LocalDateTime
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CompareServiceSpec extends AnyFlatSpec with Matchers {

  // Simple tests for equality
  def now = LocalDateTime.now
  def rootNode = new DirItem("/", "dir1", 2, now, false, None)
  def emptyDir = new DirTree(rootNode, Seq())

  "A DirTree when compared to itself" should "return a pair of its copies" in {
    val tree = emptyDir
    val (left, right) = CompareService.compareTrees(tree, tree)
    
    left.node.name shouldEqual tree.node.name
    right.node.name shouldEqual tree.node.name
    
  }

  it should "return those copies with status 'Same'" in {
    val tree = emptyDir
    val (left, right) = CompareService.compareTrees(tree, tree)

    left.node.status shouldEqual Some(DirItemStatus.Same)
    right.node.status shouldEqual Some(DirItemStatus.Same)
  }


  // Check compareNodes function
  def treeWithOneFile: DirTree = {
    val file =  new DirItem("/dir1", "file1", 462, now, true, None)
    val fileDirTree = new DirTree(file, Seq())
    new DirTree(rootNode, Seq(fileDirTree))
  }

  def twoSimilarDirItems: (DirItem, DirItem) = {
    val file =  new DirItem("/dir1", "file1", 112, now, true, None)
    val fileCopy =  new DirItem("/another/Dir", "file1", 765, now, true, None)
    (file, fileCopy)
  }

  def anotherDirItem = new DirItem("/another/Dir", "file2", 110, now, true, None)

  "Sequence of nodes when passed to compareNodes function" should "get new nodes marked as 'New'" in {
    val nodes = treeWithOneFile.children.map(_.node)
    var sequence = CompareService.compareNodes(nodes, Seq())
    sequence.length should be > 0
    sequence(0).status shouldEqual Some(DirItemStatus.New)
  }

  it should "get existings nodes marked as 'Same' (TODO: should implement Same vs Diff logic later)" in {
    val (file1, file1Copy) = twoSimilarDirItems
    var sequence = CompareService.compareNodes(Seq(file1), Seq(file1Copy, anotherDirItem))
    sequence.length should be > 0
    sequence.filter(_.name == "file1")(0).status shouldEqual Some(DirItemStatus.Same)
  }

  it should "get missing nodes marked as 'Missing'" in {
    val (file1, file1Copy) = twoSimilarDirItems    
    var sequence = CompareService.compareNodes(Seq(file1), Seq(file1Copy, anotherDirItem))
    sequence.length should be > 0
    sequence.exists(_.name == "file2") should be (true)
    sequence.filter(_.name == "file2")(0).status shouldEqual Some(DirItemStatus.Missing)
  }


  // check compareTrees function 
  "A tree with a missing file" should "get this file marked as missing" in {
    val (left, right) = CompareService.compareTrees(emptyDir, treeWithOneFile)
    left.children.length should be > 0
    left.children(0).node.status shouldEqual Some(DirItemStatus.Missing)
  }

  it should "also mark this file in the other tree as new" in {
    val (left, right) = CompareService.compareTrees(emptyDir, treeWithOneFile)
    right.children.length should be > 0
    right.children(0).node.status shouldEqual Some(DirItemStatus.New)
  }


  // TODO check recursion
  def treeWithSubDir = {
    val subDirItem = new DirItem("/dir1", "subdir2", 4, now, false, None)
    val subDir = new DirTree(subDirItem, Seq(new DirTree(anotherDirItem, Seq())))
    val (file1, file1Copy) = twoSimilarDirItems
    val rootChildren = Seq(new DirTree(file1, Seq()), subDir)
    new DirTree(rootNode, rootChildren)
  }

  "If a node (file, dir) is missing from a subdir it" should "be identified as missing" in {
    val (left, right) = CompareService.compareTrees(treeWithOneFile, treeWithSubDir)

    left.children.exists(i => i.node.name == "subdir2" && i.node.isFile == false) should be (true)
    val subDir = left.children.filter(i => i.node.name == "subdir2" && i.node.isFile == false)(0)

    subDir.children.exists(i => i.node.name == anotherDirItem.name && i.node.isFile == true) should be (true)
    val missingFile = subDir.children.filter(i => i.node.name == anotherDirItem.name && i.node.isFile == true)(0).node
    missingFile.status should be (Some(DirItemStatus.Missing))
  }
}