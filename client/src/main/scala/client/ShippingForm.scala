package client

import com.thoughtworks.binding.Binding.Constants
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{Event, HTMLElement}
import org.scalajs.jquery.jQuery
import pme123.adapters.client.ClientUtils
import pme123.adapters.client.SemanticUI.jq2semantic
import shared.ShippingOption

case class ShippingForm(wizardUIState: WizardUIState)
  extends ClientUtils {

  @dom
  def shipping: Binding[HTMLElement] = {
    val shippingData = wizardUIState.shippingData.value
      <div class="ui attached segment"
           onmouseover={_: Event =>
             // this as be done after div is rendered
             jQuery(".ui.checkbox").checkbox()}>
        <div class="grouped fields">
          <label for="shipping">Select the shipping option</label>{//
          Constants(ShippingOption.all
            .map(optionElem(_, shippingData.shippingOption.value)): _*).map(_.bind)}
        </div>
      </div>
  }

  @dom
  private def optionElem(shippingOption: ShippingOption, checked: ShippingOption) =
    <div class="field">
      <div class="ui radio checkbox">
        <input type="radio"
               name="shipping"
               value={shippingOption.name}
               checked={shippingOption == checked}
               class="hidden"
               onchange={_: Event =>
                 wizardUIState.shippingData.value.shippingOption.value = shippingOption}/>
        <label>
          {shippingOption.label}
        </label>
      </div>
    </div>
}
