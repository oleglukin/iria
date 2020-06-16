package iria.ui

import iria.model.{DirTree, DirItem, DirItemStatus}
import scalafx.scene.control.TreeItem


object UIUtils {
  
  /**
    * Get directory tree object for the given path, produce TreeModel from it
    * @param rootPath
    * @return TreeModel to be used in UI TreeTableView
    */
  def getTreeModel(tree: DirTree): TreeItem[DirItem] = {
    val root = new TreeItem[DirItem] (tree.node) { expanded = true }
    val children: Seq[TreeItem[DirItem]] = getTreeItemChildren(tree)
    children.foreach(ch => root.children.add(ch))
    root
  }


  /**
    * Get DirTree root children (leaves and branches) and construct sequence of TreeItem
    * @param root DirTree to traverse
    * @return sequence of TreeItem
    */
  def getTreeItemChildren(root: DirTree):Seq[TreeItem[DirItem]] = 
    root.children.map(x => x match {
      case x.node if x.node.isFile => new TreeItem[DirItem](x.node)
      case dir => {
        val item = new TreeItem[DirItem](dir.node) {expanded = true}
        val childItems = getTreeItemChildren(dir)
        childItems.foreach(ch => item.children.add(ch))
        item
      }
    })

  

  import scalafx.scene.control.{TreeTableView, TreeTableColumn, TreeTableRow, TreeTableCell}
  /**
    * Define diff screen tree table view to display DirItems
    */
  def getTreeTableView: TreeTableView[DirItem] = {

    val ttw = new TreeTableView[DirItem]
    val column1 = new TreeTableColumn[DirItem, String]("Name") {
      prefWidth = 270
      sortable = false
      cellValueFactory = _.value.value.value.nameProperty
    }

    val column2 = new TreeTableColumn[DirItem, String]("Size") {
      prefWidth = 80
      sortable = false
      cellValueFactory = _.value.value.value.sizeProperty
    }

    val column3 = new TreeTableColumn[DirItem, String]("Date") {
      prefWidth = 150
      sortable = false
      cellValueFactory = _.value.value.value.updateDateProperty
    }

    ttw.columns.addAll(column1, column2, column3)

    ttw
  }
}