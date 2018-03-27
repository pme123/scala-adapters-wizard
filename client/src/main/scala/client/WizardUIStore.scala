package client

import com.thoughtworks.binding.Binding.Var
import pme123.adapters.shared.Logger
import shared.StepStatus.ACTIVE
import shared.{CardType, User, WizardData, WizardStep}

trait WizardUIStore extends Logger {

  protected def wizardUIState: WizardUIState

  def changeWizardData(wizardData: WizardData) {
    info(s"UIStore: changeWizardData ${wizardData.ident}")
    wizardUIState.wizardData.value = wizardData
    wizardUIState.activeStep.value = wizardData.steps.find(_.status == ACTIVE)
  }

  def changeActiveStep(maybStep: Option[WizardStep]) {
    info(s"UIStore: changeActiveStep ${maybStep.map(_.ident)}")
    val wizardData =
      wizardUIState.wizardData.value
      .changeActiveStep(maybStep)
    wizardUIState.wizardData.value = wizardData
    wizardUIState.activeStep.value = maybStep
  }

  def changeUser(user: User) {
    info(s"UIStore: changeUser $user")
    wizardUIState.user.value = user
  }
}

case class WizardUIState(
                          wizardData: Var[WizardData] = Var(WizardData("dummyWizard"))
                          , user: Var[User] = Var(User.defaultUser)
                          , activeStep: Var[Option[WizardStep]] = Var(None)
                          , shippingData: Var[ShippingData] = Var(ShippingData())
                          , billingData: Var[BillingData] = Var(BillingData(Var(CardType.VISA)))
                        )

