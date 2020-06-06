package iria

import scalafx.stage.Stage
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.layout.GridPane
import scalafx.scene.control.{Label, TextField, Button}
import javafx.event.ActionEvent
import scalafx.stage.DirectoryChooser
import iria.model.CompareConfig
import scalafx.application.JFXApp.PrimaryStage

class CompareConfigScreen (var config: CompareConfig) {

  val label = new Label("Select folders to compare")

  val dirChoser = new DirectoryChooser { title = "Select folder to compare" }

  val leftFolderLabel = new Label("Left")
  val leftFolderText = new TextField {
    text = config.left
    onMouseClicked = (e: javafx.scene.input.MouseEvent) => {
      text = dirChoser.showDialog(stage).getAbsolutePath // TODO handle escape key pressed
    }
  }

  val rightFolderLabel = new Label("Right")
  val rightFolderText = new TextField {
    text = config.right
    onMouseClicked = (e: javafx.scene.input.MouseEvent) => {
      text = dirChoser.showDialog(stage).getAbsolutePath // TODO handle escape key pressed
    }
  }

  val compareButton = new Button("Compare"){
    onAction = (event: ActionEvent) => {
    config.left = leftFolderText.getText
    config.right = rightFolderText.getText

    // TODO open other stage and produce results
    import scalafx.scene.control.Alert // remove this
    import scalafx.scene.control.Alert.AlertType
    new Alert(AlertType.Information, s"${config.left}, ${config.right}").showAndWait()
    }
  }
  
  val stage: Stage = new Stage() {
    title = "Iria - Folder Compare"

    maxWidth = 800
    maxHeight = 360
    minWidth = 450
    minHeight = 230

    scene = new Scene {
      stylesheets.add("compareconfig.css")
      fill = Color.rgb(38, 38, 38)
      content = new GridPane {
        id = "gridPane"

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