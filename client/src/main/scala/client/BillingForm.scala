package client

import client.CustomSemanticUI.{Field, Form, Rule, jq2semantic}
import client.ShippingFormData.{error, info}
import com.thoughtworks.binding.Binding.{Constants, Var}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{Event, HTMLElement}
import org.scalajs.jquery.jQuery
import play.api.libs.json.{JsError, JsSuccess}
import pme123.adapters.client.ClientUtils
import shared.{Validator, _}
import BillingData.validators

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => g}
import scala.scalajs.js.JSON

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
          , Var(value.cardExpMonth)
          , Var(value.cardExpYear))
        case JsError(errors) => errors.foreach(e => error(s"Error on Path ${e._1}: ${e._2}"))
          BillingFormData()
      }
    }.getOrElse{
      info(s"No BillingFormData: $maybeStep")
      BillingFormData()
    }
  }

  def rules: Form = new Form {
    private val tuples: js.Dynamic = validators.map(v => js.Dynamic.literal(v.field -> field(v)))
      .reduce((a: js.Dynamic, b:js.Dynamic) => a + b)
    println(s"tubles: ${JSON.stringify(tuples)}")
     val fields: js.Object = js.Dynamic.literal(
          validators.head.field -> field(validators.head)
          , validators.last.field -> field(validators.last)
        )
   // val fields: js.Object = (tuples)

  }

  private def field(v: Validator[_]): Field = {
    val f = new Field {
    val identifier: String = s"${v.field}"
    val rules: js.Array[Rule] = js.Array(new Rule {
      val `type`: String = s"validate['${v.field}']"
      override val prompt = "{name} is set to \"{value}\" that is totally wrong."
    })
  }
    println(s"The field is ${JSON.stringify(f)}")
  f}
}


case class BillingForm(wizardUIState: WizardUIState)
extends ClientUtils {

  @dom
  def billing: Binding[HTMLElement] = {
    val billingData = wizardUIState.billingData.value
      <div class="ui attached segment">
        <iframe style="display:none" onload={_: Event => initForm()}></iframe>

        <div class="fields">
          <div class="required field">
            <label>First Name on Account</label>
            <input type="text"
                   id="cardFirstName"
                   placeholder="First Name ..."
                   value={billingData.cardFirstName.bind}
                   onchange={_: Event =>
                     billingData.cardFirstName.value = cardFirstName.value}/>
          </div>
          <div class="required field">
            <label>Last Name on Account</label>
            <input type="text"
                   id="cardName"
                   placeholder="Name ..."
                   value={billingData.cardName.bind}
                   onchange={_: Event =>
                     billingData.cardName.value = cardName.value}/>
          </div>
        </div>
        <div class="required field">
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
          <div class="required seven wide field">
            <label>Card Number</label>
            <input type="text"
                   id="cardNumber"
                   data:maxlength="16"
                   placeholder="Card #"
                   value={billingData.cardNumber.bind}
                   onchange={_: Event =>
                     billingData.cardNumber.value = cardNumber.value}/>
          </div>
          <div class="required three wide field">
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
              <div class="required field">
                <select class="ui fluid search dropdown"
                        id="cardExpMount"
                        value={billingData.cardExpMount.bind.ident}
                        onchange={_: Event =>
                          billingData.cardExpMount.value = Month(cardExpMount.value)}>
                  {Constants(Month.all.map(monthOption): _*).map(_.bind)}
                </select>
              </div>
              <div class="required field">
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
        <div class="ui primary submit button">Submit</div>
      </div>
  }

  // this must be called after rendering!
  private def initForm() {
    // this as be done after div is rendered
   // jQuery(".ui.form").form()
    g.$.fn.form.settings.rules.validate = { (value: js.Dynamic, field: js.Dynamic) =>
      println(s"Validate $value - $field")
    //  val result = validators.find(_.field == field)
     //   .map(_.validate(value))

false
    }


    val form = new Form {
      val fields: js.Object = js.Dynamic.literal (
        cardNumber = new Field {
          val identifier: String = "cardNumber"
          val rules: js.Array[Rule] = js.Array(new Rule {
            val `type`: String = "empty"
          })
        },
        cardCVC = new Field {
          val identifier: String = "cardCVC"
          val rules: js.Array[Rule] = js.Array(new Rule {
            val `type`: String = "validate['cardCVC']"
            override val prompt = "You must a valid CVC - see ..."
          })
        }
    )}
    println(s"Correct Form: ${JSON.stringify(form)}")

    val rules = BillingFormData.rules
    println(s"BillingFormData.rules: ${JSON.stringify(rules)}")
    jQuery(".ui.form").form(rules)


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
