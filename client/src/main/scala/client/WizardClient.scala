package client

import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.HTMLElement
import pme123.adapters.client.{AdaptersClient, DefaultView, JobProcessView}
import pme123.adapters.shared._
import slogging.{ConsoleLoggerFactory, LoggerConfig}

import scala.scalajs.js.annotation.JSExportTopLevel

/**
  * A demo that does not use a Job Process at all.
  */
case class WizardClient(context: String, websocketPath: String)
  extends AdaptersClient {

  @dom
  protected def render: Binding[HTMLElement] = {
    <div class="full-height">
      {Wizard(context, websocketPath).create().bind}{//
      css.bind}
    </div>
  }

  @dom
  private def css = <style>
    {"""
    .full-height {
      height: 100%;
      max-height:calc(100vh - 8em);
    }
  """}
  </style>

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
