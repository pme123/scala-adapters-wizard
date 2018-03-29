package client

import com.thoughtworks.binding.Binding.{Constants, Var}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{Event, HTMLElement}
import org.scalajs.jquery.jQuery
import play.api.libs.json.{JsError, JsSuccess}
import pme123.adapters.client.ClientUtils
import pme123.adapters.client.SemanticUI.jq2semantic
import pme123.adapters.shared.Logger
import shared.{ShippingData, ShippingOption, WizardStep}

case class ShippingFormData(shippingOption: Var[ShippingOption] = Var(ShippingOption.FREE))

object ShippingFormData extends Logger {

  def apply(maybeStep: Option[WizardStep]): ShippingFormData = {
    (for {
      step <- maybeStep
      data <- step.stepData
    } yield data).map { jsStep =>
      jsStep.validate[ShippingData] match {
        case JsSuccess(value, _) => ShippingFormData(Var(value.shippingOption))
        case JsError(errors) => errors.foreach(e => error(s"Error on Path ${e._1}: ${e._2}"))
          ShippingFormData()
      }
    }.getOrElse{
      info(s"No Sipping Data: $maybeStep")
      ShippingFormData()
    }
  }
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
