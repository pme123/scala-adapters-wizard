package client

import org.scalajs.jquery.JQuery
import play.api.libs.json.JsValue
import pme123.adapters.client.SemanticUI

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  *
  */
object CustomSemanticUI {

  // Monkey patching JQuery
  @js.native
  trait SemanticJQuery extends SemanticUI.SemanticJQuery {
    def form(params: js.Any*): SemanticJQuery = js.native
    def fn: SemanticJQuery = js.native
    def settings: SemanticJQuery = js.native
  }

  // Monkey patching JQuery with implicit conversion
  implicit def jq2semantic(jq: JQuery): SemanticJQuery = jq.asInstanceOf[SemanticJQuery]

  trait Form extends js.Object {
    def fields: js.Object
  }

  trait Field extends js.Object {
    def identifier: String
    def rules: js.Array[Rule]
  }

  trait Rule extends js.Object {
    def `type`: String
    def prompt: js.UndefOr[String] = js.undefined
  }

}