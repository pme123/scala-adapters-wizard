package shared

import julienrf.json.derived
import play.api.libs.json.{Json, OFormat}

case class WizardData(ident: WizardData.WizardIdent
                      , steps: List[WizardStep] = Nil)

import pme123.adapters.shared.AdaptersExtensions._

object WizardData {

  val defaultWizardData = WizardData("Empty Wizard")

  type WizardIdent = String

  def extractIdent(webPath: String): String =
    webPath.split("/")
      .filter(_.nonBlank)
      .head

  implicit val jsonFormat: OFormat[WizardData] = Json.format[WizardData]

}

case class WizardStep(ident: String
                      , title: String
                      , descr: String
                      , status: StepStatus = StepStatus.NONE)

object WizardStep {
  val shippingIdent = "shipping"
  val billingIdent = "billing"
  val confirmOrderIdent = "confirm-order"

  implicit val jsonFormat: OFormat[WizardStep] = Json.format[WizardStep]
}

sealed trait StepStatus {
  def ident: String
}

object StepStatus {
  implicit val jsonFormat: OFormat[StepStatus] = derived.oformat[StepStatus]()

  case object NONE extends StepStatus {
    val ident: String = ""
  }

  case object ACTIVE extends StepStatus {
    val ident: String = "active"
  }

  case object COMPLETED extends StepStatus {
    val ident: String = "completed"
  }

  case object DISABLED extends StepStatus {
    val ident: String = "disabled"
  }

}


