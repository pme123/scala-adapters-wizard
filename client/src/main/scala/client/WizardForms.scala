package client

import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{Event, HTMLElement}
import pme123.adapters.client.ClientUtils
import shared.WizardStep

case class WizardForms(wizardUIState: WizardUIState)
  extends WizardUIStore
    with ClientUtils {

  @dom
  def create(): Binding[HTMLElement] = {

    <div class="ui form">
      {ShippingForm(wizardUIState).shipping.bind}{//
      BillingForm(wizardUIState).billing.bind}{//
      ConfirmOrderForm(wizardUIState).confirmOrder.bind //
      }<div class="ui three bottom attached buttons">
      {backButton.bind //
      }<div class="or" data:data-text=""></div>{//
      nextButton.bind}
    </div>
    </div>
  }

  @dom
  private def nextButton = {
    val activeStep = wizardUIState.activeStep.bind
    <button class={s"ui positive ${disableNextButton(activeStep)} button"}
              onclick={_: Event =>
                goToNext()}
              data:data-tooltip="Go to the next step"
              data:data-position="top left">
        Next
      </button>
  }

  @dom
  private def backButton = {
    val activeStep = wizardUIState.activeStep.bind
    <button class={s"ui ${disableBackButton(activeStep)} button"}
            onclick={_: Event =>
              goToBack()}
            data:data-tooltip="Go to the back step"
            data:data-position="top left">
      Back
    </button>
  }

  private def disableBackButton(activeStep: Option[WizardStep]) =
    activeStep
      .find(wizardUIState.wizardData.value.hasBackStep)
      .map(_ => "")
      .getOrElse("disabled")

  private def disableNextButton(activeStep: Option[WizardStep]) =
    activeStep
      .find(wizardUIState.wizardData.value.hasNextStep)
      .map(_ => "")
      .getOrElse("disabled")

  private def goToNext() {
    val step = wizardUIState.activeStep.value
    val nextStep = step.flatMap(st => wizardUIState.wizardData.value.nextStep(st))
    changeActiveStep(nextStep)
  }

  private def goToBack() {
    val step = wizardUIState.activeStep.value
    val backStep = step.flatMap(st => wizardUIState.wizardData.value.backStep(st))
    changeActiveStep(backStep)
  }
}
