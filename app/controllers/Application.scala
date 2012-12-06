package logfront.controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  def app(path: String) = Action {
    Ok(views.html.index())
  }

  def notFound(path: String) = Action { NotFound }
}
