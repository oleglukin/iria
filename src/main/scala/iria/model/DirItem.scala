package iria.model


import java.time.LocalDateTime
import scalafx.beans.property.ReadOnlyStringProperty

case class DirItem (
  val parent: String,
  val name: ReadOnlyStringProperty,
  val size: Double,
  val updateDate: LocalDateTime
)

import javafx.beans.property.ObjectProperty
import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder
class DirItemNameFactory(val item: DirItem) extends scalafx.beans.value.ObservableValue[String, String] {

  override def delegate: javafx.beans.value.ObservableValue[String] = item.name

  override def value: String = item.name.get
}