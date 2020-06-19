package iria.ui

import iria.model.CompareConfig
import iria.logic.{CompareService, FileSystemService, TreeSort}
import javafx.event.ActionEvent
import scalafx.stage.Stage
import scalafx.scene.Scene
import scalafx.scene.control.{Label, Button}
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

  val resultLeft = UIUtils.getTreeTableView
  val resultRight = UIUtils.getTreeTableView

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
}