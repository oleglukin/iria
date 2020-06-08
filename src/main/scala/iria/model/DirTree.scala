package iria.model

/**
  * Directory tree model
  * @param node - directory or file
  * @param children - subdirectories and files
  */
class DirTree (
  val node: DirItem,
  val children: Seq[DirTree] // Consider using Option[Seq[DirTree]]
)