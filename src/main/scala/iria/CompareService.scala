package iria

import iria.model.DirItem
import iria.model.DirTree
import java.time.LocalDateTime
import scalafx.scene.control.TreeItem
import scalafx.beans.property.ReadOnlyStringProperty


object CompareService {
  
  def getTreeModel: TreeItem[DirItem] = {
    // test / mock data
    val dt = LocalDateTime.now
    val root = new TreeItem[DirItem] (new DirItem("/my/folder/", "dirX", 0, dt, false)) {expanded = true}

    val rootPath = "/my/folder/dirX"
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

  // def getDirTree(rootPath: String): DirTree = {
  //   new DirTree(new DirItem, )
  // }
}