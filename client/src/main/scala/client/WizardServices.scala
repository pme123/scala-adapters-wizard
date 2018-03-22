package client

import com.thoughtworks.binding.Binding
import org.scalajs.dom.raw.HTMLElement
import play.api.libs.json.{JsError, JsSuccess, JsValue}
import pme123.adapters.client.ClientUtils
import shared.{User, WizardData}

case class WizardServices(wizardUIState: WizardUIState, context: String)
  extends WizardUIStore
    with ClientUtils {

  def user(userName: String): Binding[HTMLElement] = {
    val apiPath = s"$context/user/$userName"

    def toUser(jsValue: JsValue) = jsValue.validate[User] match {
      case JsSuccess(user, _) =>
        changeUser(user)
        ""
      case JsError(errors) =>
        error(s"errors: $errors")
        s"Problem parsing User: ${errors.map(e => s"${e._1} -> ${e._2}")}"
    }

    callService(apiPath, toUser)
  }

  def wizard(wizardIdent: String): Binding[HTMLElement] = {
    val apiPath = s"$context/wizard/$wizardIdent"

    def toWizardData(jsValue: JsValue) = jsValue.validate[WizardData] match {
      case JsSuccess(wizardData, _) =>
        changeWizardData(wizardData)
        ""
      case JsError(errors) =>
        error(s"errors: $errors")
        s"Problem parsing WizardData: ${errors.map(e => s"${e._1} -> ${e._2}")}"
    }

    callService(apiPath, toWizardData)
  }

}
