package server

import pme123.adapters.server.entity.MissingArgumentException
import shared.{StepStatus, WizardData, WizardStep}
import WizardStep._
object WizardDataRepo {

  val wizardDataSet: Set[WizardData] = Set(
    WizardData("Delivery Wizard", List(
      WizardStep(shippingIdent,"Shipping", "Choose your shipping options", StepStatus.ACTIVE)
      , WizardStep(billingIdent,"Billing", "Enter billing information")
      , WizardStep(confirmOrderIdent,"Confirm Order", "Verify order details")

    ))
    , WizardData.defaultWizardData
  )

  def wizard(wizardIdent: String): WizardData =
    wizardDataSet.find(_.ident == wizardIdent)
    .getOrElse(throw MissingArgumentException(s"There is no WizardData with the wizardIdent '$wizardIdent'"))

}
