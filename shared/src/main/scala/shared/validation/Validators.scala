package shared.validation

import pme123.adapters.shared.AdaptersExtensions._

trait Validators {
  import Validators._

  def emptyString(value: String): Validation[String] =
    if (value.nonBlank)
      Validation.success(value)
    else
      Validation.failure(ValidateErr(errCode(EMPTY)))

  def errCode(code: String) = s"$prefix$code"

}

object Validators {
  private val prefix = "validation.err."
  val EMPTY = "empty"

}
