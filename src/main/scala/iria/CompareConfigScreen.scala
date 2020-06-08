package iria

import javafx.event.ActionEvent
import iria.model.CompareConfig
import scalafx.stage.{Stage, DirectoryChooser}
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.layout.GridPane
import scalafx.scene.control.{Label, TextField, Button}


class CompareConfigScreen (val diffScreen: DiffScreen) {

  val stage: Stage = new Stage {
    title = "Iria - Directory Compare"
    maxWidth = 800
    maxHeight = 360
    minWidth = 550
    minHeight = 230
  }

  val errormsg = "Provide directory path"
  val errorStyle = "-fx-text-inner-color: red;"
  val normalStyle = "-fx-text-inner-color: black;"

  val dirChoser = new DirectoryChooser { title = "Select directory to compare" }

  val leftDirText = createDirText(errorStyle, normalStyle)
  val rightDirText = createDirText(errorStyle, normalStyle)
  val leftBrowseButton = createBrowseButton(leftDirText, normalStyle, dirChoser, left, stage)
  val rightBrowseButton = createBrowseButton(rightDirText, normalStyle, dirChoser, right, stage)


  val compareButton = new Button("Compare") {
    onAction = (event: ActionEvent) => {
      leftDirText.getText match {
        case x if isInvalidPath(x) => {
          CompareConfig.left = None
          leftDirText.setStyle(errorStyle)
          leftDirText.text = errormsg
        }
        case v => {
          CompareConfig.left = Some(v)
        }
      }

      rightDirText.getText match {
        case x if isInvalidPath(x) => {
          CompareConfig.right = None
          rightDirText.setStyle(errorStyle)
          rightDirText.text = errormsg
        }
        case v => {
          CompareConfig.right = Some(v)
        }
      }

      if (!isInvalidPath(left) && !isInvalidPath(right)) {
        stage.hide
        diffScreen.displayElements
        diffScreen.stage.showAndWait
      }
    }
  }

  
  stage.scene = new Scene {
    stylesheets.add("compareconfig.css")
    fill = Color.rgb(38, 38, 38)
    content = new GridPane {
      id = "compareConfigGridPane"

      add(new Label("Select directories to compare"), 1, 0)
      add(new Label("Left"), 0, 1)
      add(leftDirText, 1, 1)
      add(leftBrowseButton, 2, 1)
      add(new Label("Right"), 0, 2)
      add(rightDirText, 1, 2)
      add(rightBrowseButton, 2, 2)
      add(compareButton, 1, 3)
    }
  }

  stage.show


  def left: String = CompareConfig.left.getOrElse("")
  def right: String = CompareConfig.right.getOrElse("")

  /**
    * Creates text field with a change listener (valid, error)
    * @param errorStyle style for error message
    * @param normalStyle normal (valid) text style
    * @return new TextField node
    */
  def createDirText(errorStyle: String, normalStyle: String): TextField = {
    val field = new TextField
    
    field.textProperty.addListener((observable, oldValue, newValue) => {
      newValue match {
        case x if isInvalidPath(x.toString) => field.setStyle(errorStyle)
        case _ => field.setStyle(normalStyle)
      }
    })

    field
  }


  def isInvalidPath(path: String): Boolean = Seq("", errormsg).contains(path) // TODO Consider improving path validation


  def getPath(dc: DirectoryChooser, st: Stage, default: String): String = dc.showDialog(st) match {
    case null => default
    case v => v.getAbsolutePath
  }


  /**
    * Creates bew Buttom adding directory choser and corresponding text field behaviour
    * @param textField text field node to update on button action
    * @param textStyle set text field style on action
    * @param dirChoser DirectoryChooser to use
    * @param defaultPath default path if user doesn select directory
    * @param stage use this stage to show dialog
    * @return new Buttom node
    */
  def createBrowseButton(
    textField: TextField,
    textStyle: String,
    dirChoser: DirectoryChooser,
    defaultPath: String,
    stage: Stage
    ): Button =
    new Button("Browse") {
      onAction = (event: ActionEvent) => {
        CompareConfig.left = Some(getPath(dirChoser, stage, defaultPath))
        textField.text = left
        textField.setStyle(textStyle)
      }
    }
}