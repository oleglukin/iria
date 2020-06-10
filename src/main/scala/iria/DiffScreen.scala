package iria

import iria.model.CompareConfig
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

  val resultLeft = getTreeTableView
  val resultRight = getTreeTableView

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
   * Use CompareService functions to read directory tree structures and compare them
   */
  def displayElements: Unit = {
    val leftTree = CompareService.dirTreeFromPath(getValueOrDefault(CompareConfig.left))
    val rightTree = CompareService.dirTreeFromPath(getValueOrDefault(CompareConfig.left))

    val (leftTreeCompared, rightTreeCompared) = CompareService.compare(leftTree, rightTree)

    resultLeft.setRoot(CompareService.getTreeModel(leftTree))
    resultRight.setRoot(CompareService.getTreeModel(rightTree))

    labelLeft.text = getValueOrDefault(CompareConfig.left)
    labelRight.text = getValueOrDefault(CompareConfig.right)
  }

  def getValueOrDefault(input: Option[String]): String = input match {
    case Some("") => "Not defined"
    case Some(value) => value
    case _ => "Not defined"
  }


  def getTreeTableView = {
    import iria.model.DirItem
    import scalafx.scene.control.TreeTableColumn
    
    val ttw = new TreeTableView[DirItem]
    val column1 = new TreeTableColumn[DirItem, String]("Name") {
      prefWidth = 270
      sortable = false
      cellValueFactory = _.value.value.value.nameProperty
    }

    val column2 = new TreeTableColumn[DirItem, String]("Size") {
      prefWidth = 80
      sortable = false
      cellValueFactory = _.value.value.value.sizeProperty
    }

    val column3 = new TreeTableColumn[DirItem, String]("Date") {
      prefWidth = 150
      sortable = false
      cellValueFactory = _.value.value.value.updateDateProperty
    }

    ttw.columns.addAll(column1, column2, column3)
    
    ttw
  }
}