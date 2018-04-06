package shared

import cats.data.ValidatedNel

package object validation {
  type Validation[T] = ValidatedNel[Err, T]

}
