package iria.model


import java.time.LocalDateTime
import scalafx.beans.property.ReadOnlyStringProperty

case class DirItem (
  val parent: String,
  val name: ReadOnlyStringProperty,
  val size: Double,
  val updateDate: LocalDateTime
)