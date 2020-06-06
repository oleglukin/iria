package iria

import iria.model.CompareConfig
import javafx.event.ActionEvent
import scalafx.stage.Stage
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.layout.GridPane
import scalafx.scene.control.{Label, Button}

class DiffScreen {
  val label1 = new Label("Comparison results here for folders:")
  val label2 = new Label
  val label3 = new Label

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
      stylesheets.add("diffscreen.css")
      fill = Color.rgb(38, 38, 38)
      content = new GridPane {
        id = "resultsGridPane"

        add(label1, 0, 0)
        add(label2, 0, 1)
        add(label3, 0, 2)

        add(button, 0, 3)
      }
    }
  }

  var compareConfigScreen: Option[CompareConfigScreen] = None

  def displayElements: Unit = {
    label2.text = getValueOrDefault(CompareConfig.left)
    label3.text = getValueOrDefault(CompareConfig.right)
  }

  def getValueOrDefault(input: Option[String]): String = input match {
    case Some("") => "Not selected"
    case Some(value) => value
    case _ => "Not selected"
  }
}