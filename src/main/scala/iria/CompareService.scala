package iria

import iria.model.DirItem
import iria.model.DirTree
import java.io.File
import java.time.LocalDateTime
import scalafx.scene.control.TreeItem


object CompareService {
  
  def getTreeModel(rootPath: String): TreeItem[DirItem] = {

    val tree = getDirTree(rootPath)

    val root = new TreeItem[DirItem] (tree.node) {expanded = true}

    val children: Seq[TreeItem[DirItem]] = tree.children.map(x => x match {
      case x.node if x.node.isFile => new TreeItem[DirItem](x.node)
      case dir => new TreeItem[DirItem](dir.node) {expanded = true}
    })

    println("++ children count: " + children.toArray.length)

    children.foreach(ch => root.children.add(ch))

    root
  }

  def getTreeModelMock(rootPath: String): TreeItem[DirItem] = { // TODO delete this
    // test / mock data
    val dt = LocalDateTime.now
    val root = new TreeItem[DirItem] (new DirItem("/my/folder/", "dirX", 0, dt, false)) {expanded = true}

    val mockRootPath = "/my/folder/dirX"
    val f1 = new TreeItem[DirItem] (new DirItem(rootPath, "file1.txt", 180.64, dt, true))
    val f2 = new TreeItem[DirItem] (new DirItem(rootPath, "file2.png", 6.41, dt, true))
    val subf2 = new TreeItem[DirItem] (new DirItem(rootPath, "subfolder2", 0, dt, false)) {expanded = true}
    val subf1 = new TreeItem[DirItem] (new DirItem(rootPath, "subfolder1", 0, dt, false)) {expanded = true}

    val subf1Path = rootPath + "/subfolder1"
    val subf2Path = rootPath + "/subfolder2"
    val f11 = new TreeItem[DirItem] (new DirItem(subf1Path, "pic.png", 16, dt, true))
    val f12 = new TreeItem[DirItem] (new DirItem(subf1Path, "text.txt", 914.2, dt, true))
    val f13 = new TreeItem[DirItem] (new DirItem(subf1Path, "f894.jpg", 156, dt, true))
    val f21 = new TreeItem[DirItem] (new DirItem(subf2Path, "9951635.png", 3210.02, dt, true))
    val f22 = new TreeItem[DirItem] (new DirItem(subf2Path, ".gitignore", 17, dt, true))
    root.children.addAll(f1, f2, subf1, subf2)
    subf1.children.addAll(f11, f12, f13)
    subf2.children.addAll(f21, f22)
    root
  }


  
  /** Traverse files and subdirectories and construct DirTree
    */
  def getDirTree(rootPath: String): DirTree = {
    val root = new File(rootPath) // TODO handle invalid root path

    println(s"  rootPath: ${rootPath}\n  abs path: ${root.getAbsolutePath}\n  canonical path: ${root.getCanonicalPath}\n" +
      s"  file name: ${root.getName}")

    val mockSize: Double = 16
    val mockDate: LocalDateTime = LocalDateTime.now

    val rootItem = new DirItem(rootPath, rootPath, mockSize, mockDate, false)

    val children = root match {
      case dir if dir.isDirectory => {
        dir.listFiles.map(x => {
          val node = new DirItem(x.getParent, x.getName, mockSize, mockDate, false)
          new DirTree(node, Seq()) // TODO recursively traverse subdirectories
        }).toSeq
      }
      case file => {
        val node = new DirItem(file.getParent, file.getName, mockSize, mockDate, true)
        Seq(new DirTree(node, Seq()))
      }
    }

    new DirTree(rootItem, children)
  }
}