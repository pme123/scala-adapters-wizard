package shared.validation

sealed trait Err {
  val code: String
  val msg: Option[String]
  val values: Seq[AnyRef] = Nil
}

case class ValidateErr(code: String
                       , msg: Option[String] = None)
  extends Err {
}

case class BadRequest(code: String
                      , msg: Option[String] = None)
  extends Err {
}

case class NotFound(code: String
                    , msg: Option[String] = None
                    , override val values: Seq[AnyRef] = Nil)
  extends Err
