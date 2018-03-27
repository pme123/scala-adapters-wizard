package client

import com.thoughtworks.binding.Binding.{Constants, Var}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{Event, HTMLElement}
import org.scalajs.jquery.jQuery
import pme123.adapters.client.ClientUtils
import pme123.adapters.client.SemanticUI.jq2semantic
import shared.WizardStep

import scala.scalajs.js

case class ConfirmOrderData(shippingOption: Var[Double] = Var(0))

case class ConfirmOrderForm(wizardUIState: WizardUIState)
extends ClientUtils{

  @dom
  def confirmOrder: Binding[HTMLElement] = {
    val activeStep = wizardUIState.activeStep.bind
    val billingData = wizardUIState.billingData.value
    val user = wizardUIState.user.bind
    val orderList = Seq(("Crazy little Supertoy", 99.0)
      , ("Akku CLE 123", 34.5)
      , ("Shipping", 3.20))
    if (activeStep.exists(st => st.ident == WizardStep.confirmOrderIdent))
      <div class="ui attached segment"
           onmouseover={_: Event =>
             // this as be done after div is rendered
             jQuery(".ui.dropdown").dropdown(js.Dynamic.literal(on = "hover"))}>
        <h3>Your Order</h3>
        <table class="ui basic table">
          <tbody>
            {Constants(orderList.map(pair => confirmRow(pair._1, pair._2)): _*).map(_.bind)}{//
            confirmRow("Total", orderList.map(_._2).sum, "positive").bind}
          </tbody>
        </table>
        <h3>Shipping Address</h3>
        <p>
          {s"${user.personalData.firstName} ${user.personalData.name}"}
        </p>
        <p>
          {s"${user.address.street} ${user.address.streetNr}"}
        </p>
        <p>
          {s"${user.address.city.code} ${user.address.city.name}"}
        </p>
        <h3>Shipping and Billing options</h3>
        <table class="ui basic table">
          <tbody>
            {infoRow("Card Owner", s"${billingData.cardFirstName.bind} ${billingData.cardName.bind}").bind}
            {infoRow("Card Type", s"${billingData.cardType.bind.name}").bind}
          </tbody>
        </table>

      </div>
    else <span></span>
  }

  @dom
  private def confirmRow(name: String, price: Double, rowClass:String = "") =
    <tr class={rowClass}>
      <td class="thirteen wide">
        {name}
      </td> <td class="three wide right aligned">
      {f"$price%1.2f"}
      CHF</td>
    </tr>

  @dom
  private def infoRow(label: String, value: String) =
    <tr>
      <td class="four wide">
        {label}:
      </td> <td class="twelve wide">
      {value}</td>
    </tr>

}