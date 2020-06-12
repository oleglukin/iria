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


  def get2SimilarFilesAndDir: (DirItem, DirItem, DirItem) = {
    val file1 = new DirItem("/", "file1", 2, LocalDateTime.now, true, None)
    val file2 = new DirItem("/", "file1", 17, LocalDateTime.of(2020, 6, 12, 23, 41, 59), true, None)
    val dir1 = new DirItem("/", "dir1", 43, LocalDateTime.now, false, None)
    (file1, file2, dir1)
  }

  "A DirItem" should "match itself" in {
    val file1 = new DirItem("/", "file1", 2, LocalDateTime.now, true, None)
    DirItem.matchTogether(file1, file1) should be (true)
  }

  it should "match another if its name and type (isFile) is the same" in {
    val (file1, file2, dir) = get2SimilarFilesAndDir
    DirItem.matchTogether(file1, file2) should be (true)
  }

  it should "not match another if their names are different" in {
    val file1 = new DirItem("/", "file1", 2, LocalDateTime.now, true, None)
    val file2 = new DirItem("/", "file2", 2, LocalDateTime.now, true, None)
    DirItem.matchTogether(file1, file2) should be (false)
  }

  it should "not match another if their types (isFile) are different" in {
    val (file1, file2, dir) = get2SimilarFilesAndDir
    DirItem.matchTogether(file1, dir) should be (false)
  }

  
  "A DirItem" should "exist in sequence of other DirItems if there is an item that matches it" in {
    val (file1, file2, dir) = get2SimilarFilesAndDir
    val sequence: Seq[DirItem] = Seq(file1, file2, dir)
    file1.existsIn(sequence) should be (true)
  }

  it should "not exist in an empty sequence" in {
    val file1 = new DirItem("/", "file1", 2, LocalDateTime.now, true, None)
    file1.existsIn(Seq()) should be (false)
  }

  it should "not exist in a sequence without matching elements" in {
    val file1 = new DirItem("/", "file1", 2, LocalDateTime.now, true, None)
    val file2 = new DirItem("/", "anotherFile", 2, LocalDateTime.now, true, None)
    file1.existsIn(Seq(file2)) should be (false)
  }
}