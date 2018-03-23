package client

import com.thoughtworks.binding.Binding.Constants
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.HTMLElement
import pme123.adapters.client.ClientUtils
import shared.{User, WizardData, WizardStep}

case class Wizard(context: String, websocketPath: String)
  extends WizardUIStore
    with ClientUtils {

  // init the store
  protected val wizardUIState: WizardUIState = WizardUIState()
  changeWizardData(WizardData(websocketPath))

  val wizardServices = WizardServices(wizardUIState, "")

  @dom
  def create(): Binding[HTMLElement] =
    <div>
      {adapterHeader.bind}{//
      wizardForm.bind}
    </div>

  // 2. level of abstraction
  // **************************

  @dom
  private def adapterHeader = {

    <div class="ui main fixed borderless menu">
      {faviconElem.bind}{//
      headerTitle.bind}<div class="right menu">
      {wizardServices.user(User.extractUserName(websocketPath)).bind}{//
      wizardServices.wizard(WizardData.extractIdent(websocketPath)).bind}{//
      userTitle.bind}
    </div>
    </div>
  }

  @dom
  private def wizardForm =
    <div class="ui main container">
      <div class="ui segments">
        {steps.bind}<div class="ui attached segment">
        {WizardForms(wizardUIState).create().bind}
      </div>
      </div>
    </div>

  // 3. level of abstraction
  // **************************

  @dom
  private def steps = {
    val data = wizardUIState.wizardData.bind
    <div class="ui ordered mini top attached steps">
      {Constants(data.steps.map(wizardStep): _*).map(_.bind)}
    </div>
  }

  @dom
  private def wizardStep(step: WizardStep) =
    <div class={s"${step.status.ident} step"}>
      <div class="content">
        <div class="title">
          {step.title}
        </div>
        <div class="description">
          {step.descr}
        </div>
      </div>
    </div>

  @dom
  private def headerTitle = {
    val data = wizardUIState.wizardData.bind
    <div class="ui header item">
      {s"Wizard: ${data.ident}"}
    </div>
  }

  @dom
  private def userTitle = {
    val user = wizardUIState.user.bind
    <span
    class="tooltip"
    data:data-tooltip={user.userName}
    data:data-position="bottom right">
      <div class="ui header item">
        <img class="ui small circular image"
             src={staticAsset(s"/images/${user.avatar}")}></img>
      </div>
    </span>
  }
}
