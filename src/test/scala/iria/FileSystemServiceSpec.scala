package iria

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import iria.logic.FileSystemService

class FileSystemServiceSpec extends AnyFlatSpec with Matchers {

  val rootPath = "/home/myname/dir1/"
  val rootPathLength = rootPath.length

  "Relative path of root path" should "be an empty string" in {
    val relative = FileSystemService.getRelativePath(rootPath, rootPathLength)
    relative should be ("")
  }

  "Relative path" should "have everything from absolute path except root path" in {
    val absolutePath = rootPath + "subdir1/subdir2/file.txt"
    val relative = FileSystemService.getRelativePath(absolutePath, rootPathLength)
    relative should be ("subdir1/subdir2/file.txt")
  }

  it should "produce right results for relative path edge case without leading slash" in {
    val root = "/home/myname/dir1"
    val absolutePath = rootPath + "file.txt"
    val relative = FileSystemService.getRelativePath(absolutePath, rootPathLength)
    relative should be ("file.txt")
  }
}