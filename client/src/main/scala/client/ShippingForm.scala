package client

import com.thoughtworks.binding.Binding.Var
import com.thoughtworks.binding.{Binding, dom}
import pme123.adapters.client.SemanticUI.jq2semantic
import org.scalajs.dom.raw.{Event, HTMLElement}
import org.scalajs.jquery.jQuery
import shared.WizardStep

case class ShippingData(shippingOption: Var[Double] = Var(0))

case class ShippingForm(wizardUIState: WizardUIState) {

  @dom
  def shipping: Binding[HTMLElement] = {
    val activeStep = wizardUIState.activeStep.bind
    val shippingData = wizardUIState.shippingData.bind
    if (activeStep.exists(st => st.ident == WizardStep.shippingIdent))
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
}
