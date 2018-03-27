package shared

import play.api.libs.json.{Json, OFormat}
import pme123.adapters.shared.AdaptersExtensions._

case class User(userName: String
                , personalData: PersonalData
                , address: Address
                , avatar: String)

object User {

  val defaultUser = User("dog"
    , PersonalData("Jon", "Doggy")
    , Address("Walterstreet", "23a"
      , City(5434, "Chur"))
    , "avatar_dog.png")

  def extractUserName(webPath: String): String =
    webPath.split("/")
      .filter(_.nonBlank).last

  implicit val jsonFormat: OFormat[User] = Json.format[User]
}

case class PersonalData(name: String, firstName: String)

object PersonalData {
  implicit val jsonFormat: OFormat[PersonalData] = Json.format[PersonalData]
}

case class Address(street: String, streetNr: String, city: City)

object Address {
  implicit val jsonFormat: OFormat[Address] = Json.format[Address]
}

case class City(code: Int, name: String)

object City {
  implicit val jsonFormat: OFormat[City] = Json.format[City]
}