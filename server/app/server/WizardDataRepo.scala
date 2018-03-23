package server

import pme123.adapters.server.entity.MissingArgumentException
import shared.{StepStatus, WizardData, WizardStep}
import WizardStep._
object WizardDataRepo {

  val wizardDataSet: Set[WizardData] = Set(
    WizardData.deliveryWizard
    , WizardData.defaultWizardData
  )

  def wizard(wizardIdent: String): WizardData =
    wizardDataSet.find(_.ident == wizardIdent)
    .getOrElse(throw MissingArgumentException(s"There is no WizardData with the wizardIdent '$wizardIdent'"))

}
