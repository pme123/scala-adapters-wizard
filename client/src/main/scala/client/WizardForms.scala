package client

import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{Event, HTMLElement}
import pme123.adapters.client.ClientUtils
import org.scalajs.jquery.jQuery
import pme123.adapters.client.SemanticUI.jq2semantic
import shared.WizardStep
import shared.WizardStep._

import scala.scalajs.js

case class WizardForms(wizardUIState: WizardUIState)
  extends WizardUIStore
    with ClientUtils {

  @dom
  def create(): Binding[HTMLElement] = {

    <div class="ui form">
      {shipping.bind}{//
      billing.bind}<div class="ui three bottom attached buttons">
      {//
      backButton.bind}<div class="or" data:data-text=""></div>{//
      nextButton.bind}
    </div>
    </div>
  }
  @dom
  private def shipping = {
    val activeStep = wizardUIState.activeStep.bind
    if (activeStep.exists(st => st.ident == shippingIdent))
      <div class="ui attached segment"
           onmouseover={_: Event =>
             // this as be done after div is rendered
             jQuery(".ui.checkbox").checkbox()}>
        <div class="grouped fields">
          <label for="shipping">Select the shipping option</label>
          <div class="field">
            <div class="ui radio checkbox">
              <input type="radio" name="shipping" checked={true} class="hidden"/>
              <label>Free shipping (2-3 days)</label>
            </div>
          </div>
          <div class="field">
            <div class="ui radio checkbox">
              <input type="radio" name="shipping" class="hidden"/>
              <label>Flat rate 3.20 CHF (1-2 days)</label>
            </div>
          </div>
          <div class="field">
            <div class="ui radio checkbox">
              <input type="radio" name="shipping" class="hidden"/>
              <label>Express delivery 9.90 CHF (2-4 Office-Hours)</label>
            </div>
          </div>
        </div>
      </div>
    else <span></span>
  }

  @dom
  private def billing = {
    val activeStep = wizardUIState.activeStep.bind
    if (activeStep.exists(st => st.ident == billingIdent))
      <div class="ui attached segment"
           onmouseover={_: Event =>
        // this as be done after div is rendered
             jQuery(".ui.dropdown").dropdown(js.Dynamic.literal(on = "hover"))
           }>
        <div class="fields">
          <div class="field">
            <label>First Name on Account</label>
            <input type="text" placeholder="First Name ..."/>
          </div>
          <div class="field">
            <label>Last Name on Account</label>
            <input type="text" placeholder="Name ..."/>
          </div>
        </div>
        <div class="field">
          <label>Card Type</label>
          <div class="ui selection dropdown">
            <input type="hidden" name="card[type]"/>
            <div class="default text">Type</div>
            <i class="dropdown icon"></i>
            <div class="menu">
              <div class="item" data:data-value="mastercard">
                <i class="mastercard icon"></i>
                Mastercard
              </div>
              <div class="item" data:data-value="visa">
                <i class="visa icon"></i>
                Visa
              </div>
              <div class="item" data:data-value="amex">
                <i class="amex icon"></i>
                American Express
              </div>
              <div class="item" data:data-value="discover">
                <i class="discover icon"></i>
                Discover
              </div>
            </div>
          </div>
        </div>
        <div class="fields">
          <div class="seven wide field">
            <label>Card Number</label>
            <input type="text" name="card[number]" data:maxlength="16" placeholder="Card #"/>
          </div>
          <div class="three wide field">
            <label>CVC</label>
            <input type="text" name="card[cvc]" data:maxlength="3" placeholder="CVC"/>
          </div>
          <div class="six wide field">
            <label>Expiration</label>
            <div class="two fields">
              <div class="field">
                <select class="ui fluid search dropdown" name="card[expire-month]">
                  <option value="">Month</option>
                  <option value="1">January</option>
                  <option value="2">February</option>
                  <option value="3">March</option>
                  <option value="4">April</option>
                  <option value="5">May</option>
                  <option value="6">June</option>
                  <option value="7">July</option>
                  <option value="8">August</option>
                  <option value="9">September</option>
                  <option value="10">October</option>
                  <option value="11">November</option>
                  <option value="12">December</option>
                </select>
              </div>
              <div class="field">
                <input type="text" name="card[expire-year]" data:maxlength="4" placeholder="Year"/>
              </div>
            </div>
          </div>
        </div>
      </div>
    else <span></span>
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
