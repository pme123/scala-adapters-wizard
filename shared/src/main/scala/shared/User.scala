package shared

import play.api.libs.json.{Json, OFormat}
import pme123.adapters.shared.AdaptersExtensions._

case class User(userName: String, avatar: String)

object User {

  val defaultUser = User("dog", "avatar_dog.png")

  def extractUserName(webPath: String): String =
    webPath.split("/")
      .filter(_.nonBlank).last

  implicit val jsonFormat: OFormat[User] = Json.format[User]
}