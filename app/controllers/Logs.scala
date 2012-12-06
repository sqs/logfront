package logfront.controllers

import play.api._
import play.api.mvc._

object Logs extends Controller {
  def index(appName: String, envName: String) = Action {
    Ok("[]").as("application/json").withHeaders(
      CACHE_CONTROL -> "no-cache"
    )
  }
}
