package iria

import iria.model.CompareConfig
import scalafx.application.Platform


object Iria extends App {

  Platform.startup(new Runnable() {
    def run = {
      val diffScreen = new DiffScreen
      val compareConfigScreen = new CompareConfigScreen(diffScreen)
      diffScreen.compareConfigScreen = Some(compareConfigScreen)
    }
  })
}