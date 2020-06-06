package iria

import javafx.event.ActionEvent
import iria.model.CompareConfig
import scalafx.stage.Stage
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.layout.GridPane
import scalafx.scene.control.{Label, TextField, Button}
import scalafx.stage.DirectoryChooser

class CompareConfigScreen (val diffScreen: DiffScreen) {

  val label = new Label("Select folders to compare")

  val dirChoser = new DirectoryChooser { title = "Select folder to compare" }

  def getPath(dc: DirectoryChooser, st: Stage, default: String): String = dc.showDialog(st) match {
    case null => default
    case v => v.getAbsolutePath
  }

  def left = CompareConfig.left.getOrElse("")
  def right = CompareConfig.right.getOrElse("")

  val leftFolderLabel = new Label("Left")
  val leftFolderText = new TextField {
    text = left
    onMouseClicked = (e: javafx.scene.input.MouseEvent) => {
      CompareConfig.left = Some(getPath(dirChoser, stage, left))
      text = left
    }
  }

  val rightFolderLabel = new Label("Right")
  val rightFolderText = new TextField {
    text = right
    onMouseClicked = (e: javafx.scene.input.MouseEvent) => {
      CompareConfig.right = Some(getPath(dirChoser, stage, right))
      text = right
    }
  }

  val compareButton = new Button("Compare"){
    onAction = (event: ActionEvent) => {
      CompareConfig.left = Some(leftFolderText.getText)
      CompareConfig.right = Some(rightFolderText.getText)

      stage.hide
      diffScreen.displayElements
      diffScreen.stage.showAndWait
    }
  }
  
  val stage: Stage = new Stage {
    title = "Iria - Folder Compare"

    maxWidth = 800
    maxHeight = 360
    minWidth = 450
    minHeight = 230

    scene = new Scene {
      stylesheets.add("compareconfig.css")
      fill = Color.rgb(38, 38, 38)
      content = new GridPane {
        id = "compareConfigGridPane"

        add(label, 1, 0)
        add(leftFolderLabel, 0, 1)
        add(leftFolderText, 1, 1)
        add(rightFolderLabel, 0, 2)
        add(rightFolderText, 1, 2)
        add(compareButton, 1, 3)
      }
    }
  }

  stage.show
}