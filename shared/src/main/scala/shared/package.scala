 // Combinator syntax

package object shared {

  val shippingIdent = "shipping"
  val billingIdent = "billing"
  val confirmOrderIdent = "confirm-order"

  val defaultWizardData = WizardData("Empty Wizard")

  val deliveryWizard = WizardData("Delivery Wizard", List(
    WizardStep(shippingIdent, "Shipping", "Choose your shipping options", Some(ShippingData().asJson), StepStatus.ACTIVE)
    , WizardStep(billingIdent, "Billing", "Enter billing information", Some(BillingData().asJson))
    , WizardStep(confirmOrderIdent, "Confirm Order", "Verify order details")
  ))

}
