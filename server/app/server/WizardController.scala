package server

import javax.inject.Inject
import play.Environment
import play.api.libs.json.Json
import play.api.mvc._
import pme123.adapters.server.boundary.{AccessControl, JobCockpitController, Secured}

class WizardController @Inject()(jobController: JobCockpitController
                                 , val accessControl: AccessControl
                                 , val cc: ControllerComponents
                                 , val env: Environment
                                )
  extends AbstractController(cc)
    with Secured {

  /* ************************************
   * Web Pages
   * ************************************/

  def wizardPage(wizard: String, user: String): Action[AnyContent] =
    jobController.customPage(s"$wizard/$user")

  /* ************************************
   * REST APIs
   * ************************************/

  def user(userName: String): Action[AnyContent] = AuthenticatedAction {
    Ok(Json.toJson(UserRepo.user(userName)))
  }

  def wizard(wizardIdent: String): Action[AnyContent] = AuthenticatedAction {
    Ok(Json.toJson(WizardDataRepo.wizard(wizardIdent)))
  }

}
