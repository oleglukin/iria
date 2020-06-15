package iria.logic

import iria.model.{DirItem, DirTree, DirItemStatus}


object CompareService {
  /**
    * Directory tree comparison logic
    * @param left
    * @param right
    * @return left and right trees compared
    */
  def compareTrees(left: DirTree, right: DirTree): (DirTree, DirTree) = {
    
    val leftSubTree: Seq[DirTree] = compareTreeSequences(left.children, right.children)
    val rightSubTree: Seq[DirTree] = compareTreeSequences(right.children, left.children)

    val nodeLeft: DirItem = left.node.addStatus(DirItemStatus.Same)
    val newLeft = new DirTree(nodeLeft, leftSubTree)

    val nodeRight: DirItem = right.node.addStatus(DirItemStatus.Same)
    val newRight = new DirTree(nodeRight, rightSubTree)

    (newLeft, newRight)
  }
  

  /**
    * Compare sequence of dir trees against other. Identify new, existing, and missing items
    * @param items sequence to process
    * @param other sequence of items to compare with
    */
  def compareTreeSequences(items: Seq[DirTree], others: Seq[DirTree]): Seq[DirTree] = {
    val existingAndNew = items.map(_ match {
      // TODO check if item with the same name exists but different (size or content)
      case ex if existsMatching(ex, others) => 
        new DirTree(
          ex.node.addStatus(DirItemStatus.Same),
          compareTreeSequences(
            ex.children,
            getCorrespondingDir(ex, others).children
          )
        )
      case nw => nw addStatus DirItemStatus.New
    })

    val missing = others.filter(other => !existsMatching(other, items))
      .map(_ addStatus DirItemStatus.Missing)
    
    existingAndNew ++ missing
  }


  /** Check if given DirTree exists in sequence of other DirTree items (match together) */
  def existsMatching(that: DirTree, others: Seq[DirTree]): Boolean = 
    others.exists(other => DirItem.matchTogether(other.node, that.node))


  /** Similar to existsMatching, but returns the matching item
    * @param that given DirTree item
    * @param others sequence of other DirTree items
    */
  def getCorrespondingDir(that: DirTree, others: Seq[DirTree]): DirTree =
    others.filter(other => DirItem.matchTogether(other.node, that.node))(0)
}