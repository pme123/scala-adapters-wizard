package client

import com.thoughtworks.binding.Binding.{Constants, Var}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{Event, HTMLElement}
import org.scalajs.jquery.jQuery
import pme123.adapters.client.ClientUtils
import pme123.adapters.client.SemanticUI.jq2semantic
import shared.{CardType, WizardStep}

import scala.scalajs.js

case class BillingData(cardType: Var[CardType] = Var(CardType.VISA)
                       , cardFirstName: Var[String] = Var("Peter")
                       , cardName: Var[String] = Var("Muster")
                      )

case class BillingForm(wizardUIState: WizardUIState)
extends ClientUtils {

  @dom
  def billing: Binding[HTMLElement] = {
    val activeStep = wizardUIState.activeStep.bind
    val billingData = wizardUIState.billingData.value
    if (activeStep.exists(st => st.ident == WizardStep.billingIdent))
      <div class="ui attached segment"
           onmouseover={_: Event =>
             // this as be done after div is rendered
             jQuery(".ui.dropdown").dropdown(js.Dynamic.literal(on = "hover"))}>
        <div class="fields">
          <div class="field">
            <label>First Name on Account</label>
            <input type="text" placeholder="First Name ..." value={billingData.cardFirstName.bind}/>
          </div>
          <div class="field">
            <label>Last Name on Account</label>
            <input type="text" placeholder="Name ..." value={billingData.cardName.bind}/>
          </div>
        </div>
        <div class="field">
          <label>Card Type</label>
          <div class="ui selection dropdown">
            <input type="hidden" name="card[type]" value={billingData.cardType.bind.ident}/>
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
  private def cardTypeElem(cardType: CardType) =
    <div class="item" data:data-value={cardType.ident}>
      <i class={s"${cardType.ident} icon"}></i>
      {cardType.name}
    </div>
}
