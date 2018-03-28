package shared

import com.thoughtworks.binding.Binding.Var
import julienrf.json.derived
import play.api.libs.json._


sealed trait OrderStepsData extends StepData

object OrderStepsData {
  implicit val jsonFormat: OFormat[OrderStepsData] = derived.oformat[OrderStepsData]()

}


case class BillingData(cardFirstName: Var[String] = Var("Peter")
                       , cardName: Var[String] = Var("Muster")
                       , cardType: Var[CardType] = Var(CardType.VISA)
                       , cardNumber: Var[String] = Var("")
                       , cardCVC: Var[String] = Var("")
                       , cardExpMount: Var[Month] = Var(Month.JAN)
                       , cardExpYear: Var[Int] = Var(2018)) extends OrderStepsData {

  lazy val asJson: JsObject =
    Json.toJson(this).as[JsObject]

}

sealed trait CardType {
  def ident: String

  def name: String
}

object CardType {
  implicit val jsonFormat: OFormat[CardType] = derived.oformat[CardType]()

  case object MASTERCARD extends CardType {
    val ident: String = "mastercard"
    val name: String = "Mastercard"
  }

  case object VISA extends CardType {
    val ident: String = "visa"
    val name: String = "Visa"
  }

  case object AMEX extends CardType {
    val ident: String = "amex"
    val name: String = "American Express"
  }

  def all = Seq(MASTERCARD, VISA, AMEX)

  def apply(ident: String): CardType = all.find(_.ident == ident).getOrElse(MASTERCARD)

}

case class Month(ident: String, label: String)

object Month {

  val JAN = Month("jan", "January")
  val FEB = Month("feb", "February")
  val MAR = Month("mar", "March")
  val APR = Month("apr", "April")
  val MAY = Month("may", "May")
  val JUN = Month("jun", "June")
  val JUL = Month("jul", "July")
  val AUG = Month("aug", "August")
  val SEP = Month("sep", "September")
  val OCT = Month("oct", "October")
  val NOV = Month("nov", "November")
  val DEC = Month("dec", "December")

  val all = Seq(JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC)

  def apply(ident: String): Month = all.find(_.ident == ident).getOrElse(JAN)

  implicit val jsonFormat: OFormat[Month] = derived.oformat[Month]()
}

case class ShippingData(shippingOption: Var[ShippingOption] = Var(ShippingOption.FREE))
  extends OrderStepsData {

  private lazy val value: JsValue = Json.toJson(this)
  lazy val asJson: JsObject =
    value.as[JsObject]
}

object ShippingData {
 // implicit val jsonFormat: OFormat[ShippingData] = Json.format[ShippingData]
}

case class ShippingOption(name: String, label: String, amount: Double = 0) {

}

object ShippingOption {
  val FREE = ShippingOption("free", "Free shipping (2-3 days)")
  val FLAT = ShippingOption("flat", "Flat rate 3.20 CHF (1-2 days)", 3.2)
  val EXPRESS = ShippingOption("free", "Express delivery 9.90 CHF (2-4 Office-Hours)", 9.9)

  val all = Seq(FREE, FLAT, EXPRESS)

  implicit val jsonFormat: OFormat[ShippingOption] = derived.oformat[ShippingOption]()

}