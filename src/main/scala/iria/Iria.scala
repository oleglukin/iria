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


object Iria {

  val compareConfig = new CompareConfig("", "")

  val compareConfigScreen: CompareConfigScreen = new CompareConfigScreen {
    config = compareConfig
  }

  //compareConfigScreen.show

}