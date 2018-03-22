package server

import pme123.adapters.server.entity.MissingArgumentException
import shared.User

object UserRepo {

  val users: Set[User] = Set(
    User("pme123", "avatar_pme.png")
    , User("dino", "avatar_dino.png")
    , User("elephant", "avatar_elephant.png")
    , User.defaultUser
  )

  def user(userName: String): User =
    users.find(_.userName == userName)
    .getOrElse(throw MissingArgumentException(s"There is no User with the userName '$userName'"))

}
