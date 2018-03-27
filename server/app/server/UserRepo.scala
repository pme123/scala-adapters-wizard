package server

import pme123.adapters.server.entity.MissingArgumentException
import shared.{Address, City, PersonalData, User}

object UserRepo {

  val users: Set[User] = Set(
    User("pme123", PersonalData("Peter", "Mayer")
      , Address("Rue de Gaulle", "1"
        , City(3423, "Lausanne"))
      , "avatar_pme.png")
    , User("dino", PersonalData("Pat", "Dino")
      , Address("Reichealle", "44"
        , City(6000, "Luzern"))
      , "avatar_dino.png")
    , User("elephant", PersonalData("Alph", "Elemant")
      , Address("Am Berg", "B"
        , City(6454, "Gurtnellen"))
      , "avatar_elephant.png")
    , User.defaultUser
  )

  def user(userName: String): User =
    users.find(_.userName == userName)
    .getOrElse(throw MissingArgumentException(s"There is no User with the userName '$userName'"))

}
