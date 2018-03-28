package client

import com.thoughtworks.binding.Binding.{Constants, Var}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{Event, HTMLElement}
import org.scalajs.jquery.jQuery
import pme123.adapters.client.ClientUtils
import pme123.adapters.client.SemanticUI.jq2semantic

case class ShippingData(shippingOption: Var[ShippingOption] = Var(ShippingOption.FREE))

case class ShippingOption(name: String, label: String, amount: Double = 0)

object ShippingOption {
  val FREE = ShippingOption("free", "Free shipping (2-3 days)")
  val FLAT = ShippingOption("flat", "Flat rate 3.20 CHF (1-2 days)", 3.2)
  val EXPRESS = ShippingOption("free", "Express delivery 9.90 CHF (2-4 Office-Hours)", 9.9)

  val all = Seq(FREE, FLAT, EXPRESS)
}

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
