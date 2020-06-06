package iria

import javafx.event.ActionEvent
import scalafx.stage.Stage
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.layout.GridPane
import scalafx.scene.control.{Label, Button}

class DiffScreen {
  val label = new Label("Comparison results here")

  val button = new Button("Select folders") {
    onAction = (event: ActionEvent) => {
      stage.hide
      compareConfigScreen match {
        case Some(screen) => screen.stage.show
        case _ => println("Error: compareConfigScreen is not defined") // TODO handle error
      }
    }
  }


  val stage: Stage = new Stage {
    title = "Iria - Folder Compare Results"

    minWidth = 650
    minHeight = 500

    scene = new Scene {
      fill = Color.rgb(100, 100, 100)
      content = new GridPane {
        id = "resultsGridPane"

        add(label, 0, 0)

        add(button, 0, 1)
      }
    }
  }

  var compareConfigScreen: Option[CompareConfigScreen] = None
}