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
import scalafx.application.Platform


object Iria extends App {

  Platform.startup(new Runnable() {
    def run = {
      println("-- In runnable")
      val compareConfig = new CompareConfig("xLeft", "xRight")

      val compareConfigScreen = new CompareConfigScreen(compareConfig)

    }
  })

  

  //compareConfigScreen.show

}