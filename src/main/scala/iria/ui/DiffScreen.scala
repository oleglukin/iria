package iria.ui

import iria.model.CompareConfig
import iria.logic.{CompareService, FileSystemService, TreeSort}
import javafx.event.ActionEvent
import scalafx.stage.Stage
import scalafx.scene.Scene
import scalafx.scene.control.{Label, Button}
import scalafx.scene.layout.{GridPane, ColumnConstraints, RowConstraints, Priority, HBox}
import scalafx.scene.layout.HBox


class DiffScreen {
  val labelLeft = new Label
  val labelRight = new Label

  val selectFoldersButton = new Button("Select folders") {
    onAction = (event: ActionEvent) => {
      stage.hide
      compareConfigScreen match {
        case Some(screen) => screen.stage.show
        case _ => println("Error: compareConfigScreen is not defined") // TODO handle error
      }
    }
  }

  val refreshButton = new Button("Refresh") {
    onAction = (event: ActionEvent) => displayElements
  }

  val toLeftButton = new Button("To Left") {onAction = onToLeftButtonAction}
  val toRightButton = new Button("To Right")

  val panelBox = new HBox(8)
  panelBox.children.addAll(selectFoldersButton, refreshButton, toRightButton, toLeftButton)

  val resultLeft = UIUtils.getTreeTableView
  val resultRight = UIUtils.getTreeTableView

  val info = new Label { id = "info" }

  val gridPane = new GridPane {
    id = "resultsGridPane"

    val column1 = new ColumnConstraints { percentWidth = 50 }
    val column2 = new ColumnConstraints { percentWidth = 50 }
    val row1 = new RowConstraints
    val row2 = new RowConstraints
    val row3 = new RowConstraints { vgrow = Priority.Always }
    columnConstraints.addAll(column1, column2)
    rowConstraints.addAll(row1, row2, row3)

    add(panelBox, 0, 0, 2, 1)

    add(labelLeft, 0, 1)
    add(resultLeft, 0, 2)

    add(labelRight, 1, 1)
    add(resultRight, 1, 2)

    add(info, 0, 3)
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
    val leftTree = FileSystemService.dirTreeFromPath(getValueOrDefault(CompareConfig.left))
    val rightTree = FileSystemService.dirTreeFromPath(getValueOrDefault(CompareConfig.right))

    val (leftTreeCompared, rightTreeCompared) = CompareService.compareTrees(leftTree, rightTree)
    val leftTreeSorted = TreeSort.sortTrees(Seq(leftTreeCompared), true)(0)
    val rightTreeSorted = TreeSort.sortTrees(Seq(rightTreeCompared), true)(0)

    resultLeft.setRoot(UIUtils.getTreeModel(leftTreeSorted))
    resultRight.setRoot(UIUtils.getTreeModel(rightTreeSorted))

    labelLeft.text = getValueOrDefault(CompareConfig.left)
    labelRight.text = getValueOrDefault(CompareConfig.right)
  }

  def getValueOrDefault(input: Option[String]): String = input match {
    case Some("") => "Not defined"
    case Some(value) => value
    case _ => "Not defined"
  }

  

  import javafx.event.EventHandler
  import iria.model.{DirItem, DirItemStatus}

  def onToLeftButtonAction: EventHandler[ActionEvent] = (event: ActionEvent) => {
    info.text = ""
    val selectedItem = resultLeft.getSelectionModel.getSelectedItem

    selectedItem match {
      case n if n == null => info.text = "Select a file on left panel"
      case dir if !dir.getValue.isFile => info.text = "Select a file on left panel (not directory)"
      case notn if notn.getValue.status != Some(DirItemStatus.New) => info.text = "Select a new file on left panel (missing from right dir)"
      case f => println(f.getValue.parent + " - " + f.getValue.name) // TODO copy file if it's new
    }

    //println(selected.name)

  }
}