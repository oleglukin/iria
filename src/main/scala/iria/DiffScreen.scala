package iria

import iria.model.CompareConfig
import javafx.event.ActionEvent
import scalafx.stage.Stage
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.layout.GridPane
import scalafx.scene.control.{Label, Button, TextArea}
import scalafx.scene.layout.HBox


class DiffScreen {
  val label1 = new Label("Comparison results here for folders:")
  val labelLeft = new Label
  val labelRight = new Label
  val resultLeft = new TextArea
  val resultRight = new TextArea

  val button = new Button("Select folders") {
    onAction = (event: ActionEvent) => {
      stage.hide
      compareConfigScreen match {
        case Some(screen) => screen.stage.show
        case _ => println("Error: compareConfigScreen is not defined") // TODO handle error
      }
    }
  }

  val hbox = new HBox(8) {
    id = "hbox"
    children.add(
      new GridPane {
        id = "resultsGridPane"

        add(button, 0, 0)

        add(label1, 0, 1)
        add(labelLeft, 0, 2)
        add(resultLeft, 0, 3)

        add(labelRight, 1, 2)
        add(resultRight, 1, 3)
      }
    )
  }


  val stage: Stage = new Stage {
    title = "Iria - Folder Compare Results"
    minWidth = 650
    minHeight = 500

    scene = new Scene {
      stylesheets.add("diffscreen.css")
      fill = Color.rgb(38, 38, 38)
      content = hbox
    }
  }

  var compareConfigScreen: Option[CompareConfigScreen] = None

  def displayElements: Unit = {
    labelLeft.text = getValueOrDefault(CompareConfig.left)
    labelRight.text = getValueOrDefault(CompareConfig.right)
  }

  def getValueOrDefault(input: Option[String]): String = input match {
    case Some("") => "Not selected"
    case Some(value) => value
    case _ => "Not selected"
  }
}