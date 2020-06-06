package iria

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import iria.model.CompareConfig
import scalafx.application.Platform


object Iria extends App {

  Platform.startup(new Runnable() {
    def run = {
      println("-- In runnable")
      val compareConfig = new CompareConfig("xLeft", "xRight")
      val diffScreen = new DiffScreen

      val compareConfigScreen = new CompareConfigScreen(compareConfig, diffScreen)
      diffScreen.compareConfigScreen = Some(compareConfigScreen)
    }
  })
}