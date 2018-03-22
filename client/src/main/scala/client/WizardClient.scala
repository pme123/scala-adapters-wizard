package client

import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.jquery.jQuery
import pme123.adapters.client.SemanticUI.{SemanticJQuery, jq2semantic}
import pme123.adapters.client.{AdaptersClient, DefaultView, JobProcessView}
import pme123.adapters.shared._
import shared.{User, WizardData}
import slogging.{ConsoleLoggerFactory, LoggerConfig}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

/**
  * A demo that does not use a Job Process at all.
  */
case class WizardClient(context: String, websocketPath: String)
  extends AdaptersClient {




  override def create(): Unit = {
    dom.render(document.body, render)
    jQuery(".ui.dropdown").dropdown(js.Dynamic.literal(on = "hover"))
    jQuery(".ui.header.item .ui.input").popup(js.Dynamic.literal(on = "hover"))

    //jQuery(".ui.radio.checkbox").checkbox(js.Dynamic.literal(on = "click"))
  }

  @dom
  protected def render: Binding[HTMLElement] = {
    <div>
      {Wizard(context, websocketPath).create().bind}
    </div>
  }

}

object WizardClient
  extends Logger {

  LoggerConfig.factory = ConsoleLoggerFactory()

  @JSExportTopLevel("client.WizardClient.main")
  def mainPage(context: String
               , websocketPath: String
               , clientType: String): Unit = {
    ClientType.fromString(clientType) match {
      case JOB_PROCESS =>
        JobProcessView(context, websocketPath).create()
      case JOB_RESULTS =>
        DefaultView("there is no JobResults page defined").create()
      case CUSTOM_PAGE =>
        WizardClient(context, websocketPath).create()
      case other => warn(s"Unexpected ClientType: $other")
    }

  }
}
