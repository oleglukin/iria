package iria

import iria.model.DirItem
import java.time.LocalDateTime
import scalafx.scene.control.TreeItem
import scalafx.beans.property.ReadOnlyStringProperty


object CompareService {
  
  def getTreeModel: TreeItem[DirItem] = {
    // test / mock data
    val dt = LocalDateTime.now
    val root = new TreeItem[DirItem] (new DirItem("/my/folder/", new ReadOnlyStringProperty(this, "name", "dirX"), 0, dt))
    val f1 = new TreeItem[DirItem] (new DirItem("/my/folder/dirX", new ReadOnlyStringProperty(this, "name", "file1.txt"), 0, dt))
    val f2 = new TreeItem[DirItem] (new DirItem("/my/folder/dirX", new ReadOnlyStringProperty(this, "name", "file2.png"), 0, dt))
    val subf1 = new TreeItem[DirItem] (new DirItem("/my/folder/dirX", new ReadOnlyStringProperty(this, "name", "subfolder1"), 0, dt))
    val subf2 = new TreeItem[DirItem] (new DirItem("/my/folder/dirX", new ReadOnlyStringProperty(this, "name", "subfolder2"), 0, dt))
    val f11 = new TreeItem[DirItem] (new DirItem("/my/folder/dirX/subfolder1", new ReadOnlyStringProperty(this, "name", "pic.png"), 0, dt))
    val f12 = new TreeItem[DirItem] (new DirItem("/my/folder/dirX/subfolder1", new ReadOnlyStringProperty(this, "name", "text.txt"), 0, dt))
    val f13 = new TreeItem[DirItem] (new DirItem("/my/folder/dirX/subfolder1", new ReadOnlyStringProperty(this, "name", "f894.jpg"), 0, dt))
    val f21 = new TreeItem[DirItem] (new DirItem("/my/folder/dirX/subfolder2", new ReadOnlyStringProperty(this, "name", "9951635.png"), 0, dt))
    val f22 = new TreeItem[DirItem] (new DirItem("/my/folder/dirX/subfolder2", new ReadOnlyStringProperty(this, "name", ".gitignore"), 0, dt))
    root.children.addAll(f1, f2, subf1, subf2)
    subf1.children.addAll(f11, f12, f13)
    subf2.children.addAll(f21, f22)
    root
  }
}