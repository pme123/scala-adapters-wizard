package client

import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{Event, HTMLElement}
import org.scalajs.jquery.jQuery
import pme123.adapters.client.ClientUtils
import shared.StepStatus
import shared.WizardStep._

import scala.scalajs.js.timers.setTimeout

case class WizardForms(wizardUIState: WizardUIState)
  extends WizardUIStore
    with ClientUtils {

  @dom
  def create(): Binding[HTMLElement] =
    <div class="ui form">
      {shipping.bind}<div class="ui three bottom attached buttons">
      {//
      backButton.bind}<div class="or" data:data-text=""></div>{//
      nextButton.bind}
    </div>
    </div>

  @dom
  private def shipping = {
    val active = wizardUIState.wizardData.bind
      .steps.exists(st => st.status == StepStatus.ACTIVE && st.ident == shippingIdent)
    if (active)
      <div class="ui attached segment">
        <div class="grouped fields">
          <label for="free">Select the shipping option</label>
          <div class="field">
            <div class="ui radio checkbox">
              <input type="radio" name="free" checked={true} class="hidden"/>
              <label>Free shipping (2-3 days)</label>
            </div>
          </div>
          <div class="field">
            <div class="ui radio checkbox">
              <input type="radio" name="flat" checked={false} class="hidden"/>
              <label>Flat rate 3.20 CHF (1-2 days)</label>
            </div>
          </div>
          <div class="field">
            <div class="ui radio checkbox">
              <input type="radio" name="premium" checked={false} class="hidden"/>
              <label>Express delivery 9.90 CHF (2-4 Office-Hours)</label>
            </div>
          </div>
        </div>
      </div>
    else <span></span>
  }

  @dom
  private def nextButton = {
        <button class="ui positive button"
              onclick={_: Event =>
                info("hit next button")}
              data:data-tooltip="Go to the next step"
              data:data-position="top left">
        Next
      </button>
  }

  @dom
  private def backButton = {
    <button class="ui disabled button"
            onclick={_: Event =>
              info("hit back button")}
            data:data-tooltip="Go to the back step"
            data:data-position="top left">
      Back
    </button>
  }
}
