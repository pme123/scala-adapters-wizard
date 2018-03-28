import com.thoughtworks.binding.Binding.Var
import play.api.libs.json.Reads._
import play.api.libs.json._ // Combinator syntax

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

  implicit val varStringFormat: OFormat[Var[String]] = new OFormat[Var[String]] {

    override def writes(o: Var[String]): JsObject =
      JsObject(Map("value" -> Json.toJson(o.value)))


    override def reads(json: JsValue): JsResult[Var[String]] =
      (json \ "value").validate[String].map(Var(_))
  }

  implicit val varIntFormat: OFormat[Var[Int]] = new OFormat[Var[Int]] {

    override def writes(o: Var[Int]): JsObject =
      JsObject(Map("value" -> Json.toJson(o.value)))


    override def reads(json: JsValue): JsResult[Var[Int]] =
      (json \ "value").validate[Int].map(Var(_))
  }

  implicit val varCardTypeFormat: OFormat[Var[CardType]] = new OFormat[Var[CardType]] {

    override def writes(o: Var[CardType]): JsObject =
      JsObject(Map("value" -> Json.toJson(o.value)))


    override def reads(json: JsValue): JsResult[Var[CardType]] =
      (json \ "value").validate[CardType].map(Var(_))
  }


  implicit val varMonthFormat: OFormat[Var[Month]] = new OFormat[Var[Month]] {

    override def writes(o: Var[Month]): JsObject =
      JsObject(Map("value" -> Json.toJson(o.value)))


    override def reads(json: JsValue): JsResult[Var[Month]] =
      (json \ "value").validate[Month].map(Var(_))
  }

  implicit val varShippingOptionFormat: OFormat[Var[ShippingOption]] = new OFormat[Var[ShippingOption]] {

    override def writes(o: Var[ShippingOption]): JsObject =
      JsObject(Map("value" -> Json.toJson(o.value)))


    override def reads(json: JsValue): JsResult[Var[ShippingOption]] =
      (json \ "value").validate[ShippingOption].map(Var(_))
  }

}
