package iria

import iria.logic.CompareService
import iria.model.{DirItem, DirTree, DirItemStatus}
import java.time.LocalDateTime
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CompareServiceSpec extends AnyFlatSpec with Matchers {

  // Check compareTrees function. Simple tests for equality
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


  // Check compareTreeSequences function
  def treeWithOneFile: DirTree = {
    val file =  new DirItem("/dir1", "file1", 462, now, true, None)
    val fileDirTree = new DirTree(file, Seq())
    new DirTree(rootNode, Seq(fileDirTree))
  }

  def twoSimilarItems: (DirTree, DirTree) = {
    val file =  new DirTree(new DirItem("/dir1", "file1", 112, now, true, None), Seq())
    val fileCopy =  new DirTree(new DirItem("/another/Dir", "file1", 765, now, true, None), Seq())
    (file, fileCopy)
  }

  def anotherItem: DirTree = new DirTree(new DirItem("/another/Dir", "file2", 110, now, true, None), Seq())

  "Sequence of nodes when passed to compareTreeSequences function" should "get new nodes marked as 'New'" in {
    var sequence = CompareService.compareTreeSequences(treeWithOneFile.children, Seq())
    sequence.length should be > 0
    sequence(0).node.status shouldEqual Some(DirItemStatus.New)
  }

  it should "get existings nodes marked as 'Same' (TODO: should implement Same vs Diff logic later)" in {
    val (file1, file1Copy) = twoSimilarItems
    var sequence = CompareService.compareTreeSequences(Seq(file1), Seq(file1Copy, anotherItem))
    sequence.length should be > 0
    sequence.map(_.node).filter(_.name == "file1")(0).status shouldEqual Some(DirItemStatus.Same)
  }

  it should "get missing nodes marked as 'Missing'" in {
    val (file1, file1Copy) = twoSimilarItems    
    var sequence = CompareService.compareTreeSequences(Seq(file1), Seq(file1Copy, anotherItem))
    sequence.length should be > 0
    sequence.map(_.node).exists(_.name == "file2") should be (true)
    sequence.map(_.node).filter(_.name == "file2")(0).status shouldEqual Some(DirItemStatus.Missing)
  }


  // Check compareTrees function
  // 1. Simple check without recursion / subdirectories
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


  // 2. Check recursion / subdirectory compare
  def treeWithSubDir = {
    val (file1, file1Copy) = twoSimilarItems
    new DirTree(
      rootNode, 
      Seq(
        file1,
        new DirTree( // subdirectory with one file in it
          new DirItem("/dir1", "subdir2", 4, now, false, None),
          Seq(anotherItem)
        )
      )
    )
  }

  "If a node (file, dir) is missing from a subdir it" should "be identified as 'missing'" in {
    val (left, right) = CompareService.compareTrees(treeWithOneFile, treeWithSubDir)

    left.children.exists(i => i.node.name == "subdir2" && i.node.isFile == false) should be (true)
    val subDir = left.children.filter(i => i.node.name == "subdir2" && i.node.isFile == false)(0)

    val predicate = (i: DirTree) => i.node.name == anotherItem.node.name && i.node.isFile == true // the file we need to check
    subDir.children.exists(predicate) should be (true)
    val missingFile = subDir.children.filter(predicate)(0).node
    missingFile.status should be (Some(DirItemStatus.Missing))
  }

  it should "be identified as 'new' in the other tree" in {
    val (left, right) = CompareService.compareTrees(treeWithOneFile, treeWithSubDir)

    val subDir = right.children.filter(i => i.node.name == "subdir2" && i.node.isFile == false)(0)

    val predicate = (i: DirTree) => i.node.name == anotherItem.node.name && i.node.isFile == true // the file we need to check
    subDir.children.exists(predicate) should be (true)
    val newFile = subDir.children.filter(predicate)(0).node
    newFile.status should be (Some(DirItemStatus.New))
  }

  it should "be identified the same way for any level of nesting, e.g. 3rd level" in {
    val nestedTreeWithOneFile = new DirTree(
      new DirItem("/", "dir0", 4, now, false, None),
      Seq(treeWithOneFile)
    )

    val nestedTreeWithSubDir = new DirTree(
      new DirItem("/", "dir0", 4, now, false, None),
      Seq(treeWithSubDir)
    )

    val (left, right) = CompareService.compareTrees(nestedTreeWithOneFile, nestedTreeWithSubDir)

    val subSubDir = left.children.filter(i => i.node.isFile == false)(0)
      .children.filter(i => i.node.isFile == false)(0)

    val predicate = (i: DirTree) => i.node.name == anotherItem.node.name && i.node.isFile == true // the file we need to check

    subSubDir.children.exists(predicate) should be (true)
    val missingFile = subSubDir.children.filter(predicate)(0).node
    missingFile.status should be (Some(DirItemStatus.Missing))
  }
}