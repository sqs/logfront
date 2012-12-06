package logfront.controllers

import com.amazonaws.services.elasticbeanstalk.model._
import play.api._
import play.api.libs.json._
import play.api.mvc._
import scala.collection.JavaConversions._

object Applications extends Controller with AWSClient {
  def index = Action {
    Ok(Json.toJson(
      elasticBeanstalkClient.describeApplications(
        new DescribeApplicationsRequest()
      ).getApplications.map { app =>
        Json.obj(
          "applicationName" -> app.getApplicationName
        )
      }
    )).as("application/json").withHeaders(
      CACHE_CONTROL -> "no-cache"
    )
  }
}
