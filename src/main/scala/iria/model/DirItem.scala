package iria.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scalafx.beans.property.StringProperty

case class DirItem (
  val parent: String, // TODO this might not be requried. Relative path starting from root
  val name: String,
  val size: Double,
  val updateDate: LocalDateTime,
  val isFile: Boolean
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
}