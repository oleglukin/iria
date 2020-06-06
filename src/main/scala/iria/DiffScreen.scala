package iria

import iria.model.CompareConfig
import javafx.event.ActionEvent
import scalafx.stage.Stage
import scalafx.scene.{Scene, Group}
import scalafx.scene.paint.Color
import scalafx.scene.layout.GridPane
import scalafx.scene.control.{Label, Button, TextArea}
import scalafx.scene.layout.ColumnConstraints


class DiffScreen {
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

  val column1 = new ColumnConstraints
  column1.setPercentWidth(50)
  val column2 = new ColumnConstraints
  column2.setPercentWidth(50)

  val gridPane = new GridPane {
    id = "resultsGridPane"
    columnConstraints.addAll(column1, column2)

    add(button, 0, 0)

    add(labelLeft, 0, 1)
    add(resultLeft, 0, 2)

    add(labelRight, 1, 1)
    add(resultRight, 1, 2)
  }


  val theScene = new Scene {
    stylesheets.add("diffscreen.css")
    fill = Color.rgb(38, 38, 38) // #262626
    root = gridPane
  }



  val stage: Stage = new Stage {
    title = "Iria - Folder Compare Results"
    minWidth = 650
    minHeight = 500
    scene = theScene
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