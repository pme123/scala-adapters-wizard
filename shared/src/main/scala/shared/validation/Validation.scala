package shared.validation

import cats.data._

object Validation {

  def success[T](t: T): Validation[T] = Validated.valid(t)

  def failure[T](err: Err): Validation[T] = Validated.invalidNel(err)
}

