package client

import com.thoughtworks.binding.Binding.Var
import pme123.adapters.shared.Logger
import shared.{User, WizardData}

trait WizardUIStore extends Logger {

  protected def wizardUIState: WizardUIState

  def changeWizardData(wizardData: WizardData) {
    info(s"UIStore: changeWizardData ${wizardData.ident}")
    wizardUIState.wizardData.value = wizardData
  }

  def changeUser(user: User) {
    info(s"UIStore: changeUser $user")
    wizardUIState.user.value = user
  }
}

case class WizardUIState(
                          wizardData: Var[WizardData] = Var(WizardData("dummyWizard"))
                          , user: Var[User] = Var(User.defaultUser)
                        )

