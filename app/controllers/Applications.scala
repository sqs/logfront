package logfront.controllers

import play.api._
import play.api.mvc._

object Applications extends Controller {
  def index = Action {
    Ok("[]").as("application/json").withHeaders(
      CACHE_CONTROL -> "no-cache"
    )
  }
}
