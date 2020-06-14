package iria.model

/**
  * Directory tree model
  * @param node - directory or file
  * @param children - subdirectories and files
  */
class DirTree (
  val node: DirItem,
  val children: Seq[DirTree] // Consider using Option[Seq[DirTree]]
) {

  def addStatusToSeq(sequence: Seq[DirTree], status: DirItemStatus.Value): Seq[DirTree] =
    sequence.map(
      c => new DirTree(
        c.node.addStatus(status),
        addStatusToSeq(c.children, status)
      )
    )

  def addStatus(status: DirItemStatus.Value) = new DirTree(
    node.addStatus(status),
    addStatusToSeq(children, status)
  )
}