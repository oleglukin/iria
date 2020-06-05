package iria

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.layout.GridPane
import scalafx.scene.control.{Label, TextField, Button}
import iria.model.CompareConfig
import javafx.event.ActionEvent
import scalafx.stage.DirectoryChooser


object Iria extends JFXApp {

  val compareConfig = CompareConfig

  val label = new Label("Select folders to compare")

  val dirChoser = new DirectoryChooser { title = "Left folder" }

  val leftFolderLabel = new Label("Left")
  val leftFolderText = new TextField {
    onMouseClicked = (e: javafx.scene.input.MouseEvent) => {
      text = dirChoser.showDialog(stage).getAbsolutePath
    }
  }

  val rightFolderLabel = new Label("Right")
  val rightFolderText = new TextField {
    onMouseClicked = (e: javafx.scene.input.MouseEvent) => {
      text = dirChoser.showDialog(stage).getAbsolutePath
    }
  }

  val compareButton = new Button("Compare")
  compareButton.onAction = (event: ActionEvent) => {
    import scalafx.scene.control.Alert
    import scalafx.scene.control.Alert.AlertType
    new Alert(AlertType.Information, "Hello Dialogs!!!").showAndWait()
  }

  stage = new PrimaryStage {
    title = "Iria - Folder Compare"

    maxWidth = 800
    maxHeight = 360
    minWidth = 450
    minHeight = 230
    
    scene = new Scene {
      stylesheets.add("compareconfig.css")
      fill = Color.rgb(38, 38, 38)
      content = new GridPane {
        hgap = 10
        vgap = 10
        padding = Insets(50, 100, 50, 50)

        add(label, 1, 0)
        add(leftFolderLabel, 0, 1)
        add(leftFolderText, 1, 1)
        add(rightFolderLabel, 0, 2)
        add(rightFolderText, 1, 2)
        add(compareButton, 1, 3)
      }
    }
  }
}