package shared

import julienrf.json.derived
import play.api.libs.json.{Json, OFormat}
import shared.StepStatus.{ACTIVE, NONE}
import shared.WizardStep.{billingIdent, confirmOrderIdent, shippingIdent}

case class WizardData(ident: WizardData.WizardIdent
                      , steps: List[WizardStep] = Nil) {

  def hasNextStep(step: WizardStep): Boolean =
    nextStep(step).nonEmpty

  def nextStep(step: WizardStep): Option[WizardStep] =
    steps.dropWhile(_.ident != step.ident) match {
      case _ :: next :: _ => Some(next)
      case _ => None
    }

  def hasBackStep(step: WizardStep): Boolean =
    backStep(step).nonEmpty

  def backStep(step: WizardStep): Option[WizardStep] =
    steps.takeWhile(_.ident != step.ident) match {
      case Nil => None
      case rest => Some(rest.last)
    }

  def changeActiveStep(maybeStep: Option[WizardStep]): WizardData = {
    val activeStep = maybeStep.map(_.copy(status = ACTIVE))
    val inactiveStep = steps.find(_.status == ACTIVE).map(_.copy(status = NONE))

    (for {
      active <- activeStep
      inactive <- inactiveStep
    } yield {
      changeStep(active)
        .changeStep(inactive)
    }).getOrElse(this)
  }

  def changeStep(step: WizardStep): WizardData = {
    val newSteps = steps.foldLeft(List[WizardStep]())((a, b) => if (b.ident == step.ident) a :+ step else a :+ b)
    copy(steps = newSteps)
  }


}

import pme123.adapters.shared.AdaptersExtensions._

object WizardData {

  val defaultWizardData = WizardData("Empty Wizard")

  val deliveryWizard = WizardData("Delivery Wizard", List(
    WizardStep(shippingIdent, "Shipping", "Choose your shipping options", StepStatus.ACTIVE)
    , WizardStep(billingIdent, "Billing", "Enter billing information")
    , WizardStep(confirmOrderIdent, "Confirm Order", "Verify order details")
  ))

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

sealed trait CardType {
  def ident: String

  def name: String
}

object CardType {
  implicit val jsonFormat: OFormat[CardType] = derived.oformat[CardType]()

  case object MASTERCARD extends CardType {
    val ident: String = "mastercard"
    val name: String = "Mastercard"
  }

  case object VISA extends CardType {
    val ident: String = "visa"
    val name: String = "Visa"
  }

  case object AMEX extends CardType {
    val ident: String = "amex"
    val name: String = "American Express"
  }

  def all = Seq(MASTERCARD, VISA, AMEX)

  def apply(ident:String): CardType = all.find(_.ident == ident).getOrElse(MASTERCARD)

}


