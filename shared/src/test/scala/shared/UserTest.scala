package shared

import play.api.libs.json.Json
import shared.User._

class UserTest extends UnitTest {

  "User" should "be marshaled and un-marshaled correctly" in {

    Json.toJson(defaultUser).validate[User].get should be(defaultUser)
  }
}
