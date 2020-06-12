package iria.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scalafx.beans.property.StringProperty

case class DirItem (
  val parent: String, // TODO this might not be requried. Relative path starting from root
  val name: String,
  val size: Double,
  val updateDate: LocalDateTime,
  val isFile: Boolean,  // true for files, false for directories
  val status: Option[DirItemStatus.Value]
) {
  val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  val nameProperty = StringProperty(name)
  
  val sizeProperty = StringProperty(isFile match {
    case true => size.toString
    case false => "" // consider displaying directory size as well
  })

  val updateDateProperty = StringProperty(isFile match {
    case true => updateDate.format(formatter)
    case false => "" // consider displaying directory update date as well
  })

  def addStatus(status: DirItemStatus.Value) = new DirItem(parent, name, size, updateDate, isFile, Some(status))

  

  /**
    * Check if other collection of DirItems contains similar to this
    * @param nodes sequence of other nodes (DirItems)
    */
  def existsIn(nodes: Seq[DirItem]): Boolean = {
    nodes.exists(di => DirItem.matchTogether(di, this))
  }
}

object DirItem {
  // TODO also compare by file size and update date (later by contents)
  def matchTogether(one: DirItem, another: DirItem) = another match {
    case same if one.name == same.name && one.isFile == same.isFile => true
    case _ => false
  }
}


object DirItemStatus extends Enumeration {
  val New = Value     // item exists in this tree but not in the other directory tree
  val Missing = Value // item is not in this tree but exists in the other
  val Differs = Value // item exists in both trees but is different (content, size, update date)
  val Same = Value    // there is no difference
}