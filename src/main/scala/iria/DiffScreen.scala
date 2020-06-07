package iria

import iria.model.CompareConfig
import iria.model.DirItem
import javafx.event.ActionEvent
import scalafx.stage.Stage
import scalafx.scene.Scene
import scalafx.scene.control.{Label, Button, TreeTableView}
import scalafx.scene.layout.{GridPane, ColumnConstraints, RowConstraints, Priority}


class DiffScreen {
  val labelLeft = new Label
  val labelRight = new Label

  val button = new Button("Select folders") {
    onAction = (event: ActionEvent) => {
      stage.hide
      compareConfigScreen match {
        case Some(screen) => screen.stage.show
        case _ => println("Error: compareConfigScreen is not defined") // TODO handle error
      }
    }
  }

  val resultLeft = getTreeView
  resultLeft.setRoot(CompareService.getTreeModel)
  val resultRight = getTreeView
  resultRight.setRoot(CompareService.getTreeModel)
  

  val gridPane = new GridPane {
    id = "resultsGridPane"

    val column1 = new ColumnConstraints { percentWidth = 50 }
    val column2 = new ColumnConstraints { percentWidth = 50 }
    val row1 = new RowConstraints
    val row2 = new RowConstraints
    val row3 = new RowConstraints { vgrow = Priority.Always }
    columnConstraints.addAll(column1, column2)
    rowConstraints.addAll(row1, row2, row3)

    add(button, 0, 0)

    add(labelLeft, 0, 1)
    add(resultLeft, 0, 2)

    add(labelRight, 1, 1)
    add(resultRight, 1, 2)
  }


  val stage: Stage = new Stage {
    title = "Iria - Folder Compare Results"
    minWidth = 1000
    minHeight = 700
    scene = new Scene {
      stylesheets.add("diffscreen.css")
      root = gridPane
    }
  }

  var compareConfigScreen: Option[CompareConfigScreen] = None

  /** Display/render diff screen UI elements
    */
  def displayElements: Unit = {
    labelLeft.text = getValueOrDefault(CompareConfig.left)
    labelRight.text = getValueOrDefault(CompareConfig.right)
  }

  def getValueOrDefault(input: Option[String]): String = input match {
    case Some("") => "Not defined"
    case Some(value) => value
    case _ => "Not defined"
  }


  def getTreeView = {

    import scalafx.scene.control.TreeTableColumn
    import java.time.LocalDateTime
    
    val tw = new TreeTableView[DirItem]
    val column1 = new TreeTableColumn[DirItem, String] {
      text = "Name"
      prefWidth = 150
      cellValueFactory = {p => new ov(p.value.value.value) }
    }
    val column2 = new TreeTableColumn[DirItem, Double]("size")
    val column3 = new TreeTableColumn[DirItem, LocalDateTime]("date")

    tw.columns.addAll(column1, column2, column3)
    tw
  }
}

import javafx.beans.property.ObjectProperty
import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder
class ov(val item: DirItem) extends scalafx.beans.value.ObservableValue[String, String] {

  override def delegate: javafx.beans.value.ObservableValue[String] = item.name

  override def value: String = item.name.get
}