package client

import client.ShippingFormData.{error, info}
import com.thoughtworks.binding.Binding.{Constants, Var}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{Event, HTMLElement}
import org.scalajs.jquery.jQuery
import play.api.libs.json.{JsError, JsSuccess}
import pme123.adapters.client.ClientUtils
import pme123.adapters.client.SemanticUI.jq2semantic
import shared._

import scala.scalajs.js

case class BillingFormData(cardFirstName: Var[String] = Var("Peter")
                           , cardName: Var[String] = Var("Muster")
                           , cardType: Var[CardType] = Var(CardType.VISA)
                           , cardNumber: Var[String] = Var("")
                           , cardCVC: Var[String] = Var("")
                           , cardExpMount: Var[Month] = Var(Month.JAN)
                           , cardExpYear: Var[Int] = Var(2018)
                          )

object BillingFormData {
  def apply(maybeStep: Option[WizardStep]): BillingFormData = {
    (for {
      step <- maybeStep
      data <- step.stepData
    } yield data).map { jsStep =>
      jsStep.validate[BillingData] match {
        case JsSuccess(value, _) => BillingFormData(Var(value.cardFirstName)
          , Var(value.cardName)
          , Var(value.cardType)
          , Var(value.cardNumber)
          , Var(value.cardCVC)
          , Var(value.cardExpMount)
          , Var(value.cardExpYear))
        case JsError(errors) => errors.foreach(e => error(s"Error on Path ${e._1}: ${e._2}"))
          BillingFormData()
      }
    }.getOrElse{
      info(s"No BillingFormData: $maybeStep")
      BillingFormData()
    }
  }
}


case class BillingForm(wizardUIState: WizardUIState)
extends ClientUtils {

  @dom
  def billing: Binding[HTMLElement] = {
    val billingData = wizardUIState.billingData.value
      <div class="ui attached segment">
        <iframe style="display:none" onload={_: Event =>
          // this as be done after div is rendered
          jQuery(".ui.dropdown").dropdown(js.Dynamic.literal(on = "hover"))}></iframe>

        <div class="fields">
          <div class="field">
            <label>First Name on Account</label>
            <input type="text"
                   id="cardFirstName"
                   placeholder="First Name ..."
                   value={billingData.cardFirstName.bind}
                   onchange={_: Event =>
                     billingData.cardFirstName.value = cardFirstName.value}/>
          </div>
          <div class="field">
            <label>Last Name on Account</label>
            <input type="text"
                   id="cardName"
                   placeholder="Name ..."
                   value={billingData.cardName.bind}
                   onchange={_: Event =>
                     billingData.cardName.value = cardName.value}/>
          </div>
        </div>
        <div class="field">
          <label>Card Type</label>
          <div class="ui selection dropdown">
            <input type="hidden"
                   id="cardType"
                   value={billingData.cardType.bind.ident}
                   onchange={_: Event =>
                     billingData.cardType.value = CardType(s"${cardType.value}")}/>
            <div class="default text">Type</div>
            <i class="dropdown icon"></i>
            <div class="menu">
              {Constants(CardType.all.map(cardTypeElem): _*).map(_.bind)}
            </div>
          </div>
        </div>
        <div class="fields">
          <div class="seven wide field">
            <label>Card Number</label>
            <input type="text"
                   id="cardNumber"
                   data:maxlength="16"
                   placeholder="Card #"
                   value={billingData.cardNumber.bind}
                   onchange={_: Event =>
                     billingData.cardNumber.value = cardNumber.value}/>
          </div>
          <div class="three wide field">
            <label>CVC</label>
            <input type="text"
                   id="cardCVC"
                   data:maxlength="3"
                   placeholder="CVC"
                   value={billingData.cardCVC.bind}
                   onchange={_: Event =>
                     billingData.cardCVC.value = cardCVC.value}/>
          </div>
          <div class="six wide field">
            <label>Expiration</label>
            <div class="two fields">
              <div class="field">
                <select class="ui fluid search dropdown"
                        id="cardExpMount"
                        value={billingData.cardExpMount.bind.ident}
                        onchange={_: Event =>
                          billingData.cardExpMount.value = Month(cardExpMount.value)}>
                  {Constants(Month.all.map(monthOption): _*).map(_.bind)}
                </select>
              </div>
              <div class="field">
                <input type="text"
                       id="cardExpYear"
                       data:maxlength="4" placeholder="Year"
                       value={billingData.cardExpYear.bind.toString}
                       onchange={_: Event =>
                         billingData.cardExpYear.value = cardExpYear.value.toInt}/>
              </div>
            </div>
          </div>
        </div>
      </div>
  }

  @dom
  private def cardTypeElem(cardType: CardType) =
    <div class="item" data:data-value={cardType.ident}>
      <i class={s"${cardType.ident} icon"}></i>
      {cardType.name}
    </div>

  @dom
  private def monthOption(month: Month) =
    <option value={month.ident}>
      {month.label}
    </option>
}
