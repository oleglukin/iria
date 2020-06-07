package iria.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scalafx.beans.property.ReadOnlyStringProperty

case class DirItem (
  val parent: String, // TODO this might not be requried. Relative path starting from root
  val name: ReadOnlyStringProperty,
  val size: Double,
  val updateDate: LocalDateTime,
  val isFile: Boolean
) {
  def sizeString: ReadOnlyStringProperty = new ReadOnlyStringProperty(this, "size", size.toString)

  val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
  def updateDateString: ReadOnlyStringProperty = new ReadOnlyStringProperty(this, "size", updateDate.format(formatter))
}


class DirItemNameFactory(val item: DirItem) extends scalafx.beans.value.ObservableValue[String, String] {
  override def delegate: javafx.beans.value.ObservableValue[String] = item.name
  override def value: String = item.name.get
}

class DirItemSizeFactory(val item: DirItem) extends scalafx.beans.value.ObservableValue[String, String] {
  override def delegate: javafx.beans.value.ObservableValue[String] = item.isFile match {
    case true => item.sizeString
    case false => new ReadOnlyStringProperty(this, "size", "") // consider displaying folder size as well
  }

  override def value: String = item.sizeString.get
}

class DirItemDateFactory(val item: DirItem) extends scalafx.beans.value.ObservableValue[String, String] {
  override def delegate: javafx.beans.value.ObservableValue[String] = item.isFile match {
    case true => item.updateDateString
    case false => new ReadOnlyStringProperty(this, "updateDate", "") // consider displaying folder update date as well
  }

  override def value: String = item.sizeString.get
}