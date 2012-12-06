package logfront.controllers

import play.api._
import play.api.mvc._

object Logs extends Controller {
  def get(host: String, logName: String) = Action {
    Ok("LOG123").as("text/plain").withHeaders(
      CACHE_CONTROL -> "no-cache"
    )
  }
}
