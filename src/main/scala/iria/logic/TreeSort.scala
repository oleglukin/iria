package iria.logic

import iria.model.DirTree

object TreeSort {

  /**
    * Sort Directory tree (all levels) alphanumerically
    * @param tree given tree to sort
    * @param filesFirst flag to control if files should be before dirs or wise versa
    */
  def sortTrees(trees: Seq[DirTree], filesFirst: Boolean): Seq[DirTree] = {
    val files = trees.filter(_.node.isFile).sortBy(_.node.name)
    val dirs = trees.filter(!_.node.isFile).sortBy(_.node.name)
      .map(t => new DirTree(t.node, sortTrees(t.children, filesFirst)))

    if (filesFirst) files ++ dirs else dirs ++ files
  }
}