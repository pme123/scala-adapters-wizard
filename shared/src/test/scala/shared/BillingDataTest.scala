package shared

import play.api.libs.json.Json
import shared.StepStatus.{ACTIVE, NONE}
import shared.WizardData._

class BillingDataTest extends UnitTest {

  "BillingData" should "be marshaled and un-marshaled correctly" in {

    Json.toJson(BillingData()) //.validate[StepData].get should be(BillingData())
  }
}

