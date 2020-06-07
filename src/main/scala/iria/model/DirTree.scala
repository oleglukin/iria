package iria.model

class DirTree (
  val node: DirItem,
  val children: Seq[DirTree]
)