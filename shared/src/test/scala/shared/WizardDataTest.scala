package shared

import play.api.libs.json.Json
import WizardData._
import shared.StepStatus.{ACTIVE, NONE}

class WizardDataTest extends UnitTest {

  "WizardData" should "be marshaled and un-marshaled correctly" in {

    Json.toJson(deliveryWizard).validate[WizardData].get should be(deliveryWizard)
  }

  "The first WizardStep next step" should "have a next Step" in {
    deliveryWizard.hasNextStep(deliveryWizard.steps.head) should be(true)
  }

  it should "have the 2. step as its next Step" in {
    deliveryWizard.nextStep(deliveryWizard.steps.head) should be(Some(deliveryWizard.steps(1)))
  }

  "The second WizardStep next step" should "have a next Step" in {
    deliveryWizard.hasNextStep(deliveryWizard.steps(1)) should be(true)
  }

  it should "have the 3. step as its next Step" in {
    deliveryWizard.nextStep(deliveryWizard.steps(1)) should be(Some(deliveryWizard.steps.last))
  }


  "The third WizardStep next step" should "NOT have a next Step" in {
    deliveryWizard.hasNextStep(deliveryWizard.steps.last) should be(false)
  }

  it should "have NO next Step" in {
    deliveryWizard.nextStep(deliveryWizard.steps.last) should be(None)
  }

  "The first WizardStep back step" should "NOT have a back Step" in {
    deliveryWizard.hasBackStep(deliveryWizard.steps.head) should be(false)
  }

  it should "have NO back Step" in {
    deliveryWizard.backStep(deliveryWizard.steps.head) should be(None)
  }

  "The second WizardStep back step" should "have a back Step" in {
    deliveryWizard.hasBackStep(deliveryWizard.steps(1)) should be(true)
  }

  it should "have the 1. step as its back Step" in {
    deliveryWizard.backStep(deliveryWizard.steps(1)) should be(Some(deliveryWizard.steps.head))
  }


  "The third WizardStep back step" should "have a back Step" in {
    deliveryWizard.hasBackStep(deliveryWizard.steps.last) should be(true)
  }

  it should "have the 2. step as its back Step" in {
    deliveryWizard.backStep(deliveryWizard.steps.last) should be(Some(deliveryWizard.steps(1)))
  }

  "The changed WizardStep" should "should be in the WizardData" in {
    val last = deliveryWizard.steps.last.copy(title = "Other Title")
    val data = deliveryWizard.changeStep(last)
    data.steps.last should be(last)
  }

  it should "should be the same WizardData if the ident is not found" in {
    val last = deliveryWizard.steps.last.copy(ident = "Other")
    val data = deliveryWizard.changeStep(last)
    data should be(deliveryWizard)
  }

  "The changed active WizardStep" should "should be in the WizardData" in {
    val data = deliveryWizard.changeActiveStep(Some(deliveryWizard.steps.last))
    data.steps.last.status should be(ACTIVE)
    data.steps.head.status should be(NONE)
  }
}
