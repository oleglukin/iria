package iria

import iria.model.{DirItem, DirTree, DirItemStatus}
import java.io.File
import java.time.{LocalDateTime, Instant, ZoneId}
import scalafx.scene.control.TreeItem


object CompareService {

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


  
  /**
    * Traverse files and subdirectories and construct DirTree
    * @param path directory path to start from
    * @return DirTree structure with files and subdirectories
    */
  def dirTreeFromPath(path: String): DirTree = {
    val root = new File(path) // TODO handle invalid root path
    val rootItem = new DirItem(path, root.getName, 0, LocalDateTime.now, false, None)
    val children = getDirChildren(root)
    new DirTree(rootItem, children)
  }


  /**
    * Get given root dir children: files and subfolders recursively
    * @param root the root dir
    * @return sequence of DirTree objects: leaves (files) and branches (subdirectories)
    */
  def getDirChildren(root: File): Seq[DirTree] = {
    root.listFiles.map(_ match {
      case dir if dir.isDirectory => {
        val node = new DirItem(dir.getParent, dir.getName, 0, LocalDateTime.now, false, None)
        val children = getDirChildren(dir) // recursively traverse subdirectories
        new DirTree(node, children)
      }
      case file => {
        val fileLengthBytes = file.length
        val lastModifiedDate = localDateTimeFromMs(file.lastModified)
        val node = new DirItem(file.getParent, file.getName, fileLengthBytes, lastModifiedDate, true, None)
        new DirTree(node, Seq())
      }
    }).toSeq
  }


  def localDateTimeFromMs(ms: Long) = LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneId.systemDefault())


  /**
    * Directory tree comparison logic
    * @param left
    * @param right
    * @return left and right trees compared
    */
  def compareTrees(left: DirTree, right: DirTree): (DirTree, DirTree) = {
    
    val leftChildren = left.children.map(_.node)
    val rightChildren = right.children.map(_.node)

    val leftChildrenProcessed = compareNodes(leftChildren, rightChildren)
    val rightChildrenProcessed = compareNodes(rightChildren, leftChildren)

    val leftSubTree = leftChildrenProcessed.map(new DirTree(_, Seq()))
    val rightSubTree = rightChildrenProcessed.map(new DirTree(_, Seq()))

    val nodeLeft: DirItem = left.node.addStatus(DirItemStatus.Same)
    val newLeft = new DirTree(nodeLeft, leftSubTree)

    val nodeRight = right.node.addStatus(DirItemStatus.Same)
    val newRight = new DirTree(nodeRight, rightSubTree)

    (newLeft, newRight)
  }
  

  /**
    * Compare sequence of items against other. Identify new, existing, and missing items
    * @param items sequence to process
    * @param other sequence of items to compare with
    */
  def compareNodes(items: Seq[DirItem], other: Seq[DirItem]): Seq[DirItem] = {
    val existingAndNew = items.map(_ match {
      // TODO check if item with the same name exists but different (size or content)
      case ex if other.exists(DirItem.matchTogether(_, ex)) => ex.addStatus(DirItemStatus.Same)
      case newNode => newNode.addStatus(DirItemStatus.New)
    })

    val missing = other.filter(otherNode => !items.exists(DirItem.matchTogether(_, otherNode)))
      .map(_.addStatus(DirItemStatus.Missing))
    
    existingAndNew ++ missing
  }
}