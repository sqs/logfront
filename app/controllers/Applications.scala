package logfront.controllers

import com.amazonaws.services.elasticbeanstalk.model._
import play.api._
import play.api.libs.json._
import play.api.mvc._
import scala.collection.JavaConversions._

object Applications extends Controller with AWSClient {
  def index = Action { throttled {
    Ok(Json.toJson(
      elasticBeanstalkClient.describeEnvironments(
        new DescribeEnvironmentsRequest()
      ).getEnvironments.filter { e =>
        EnvironmentStatus.valueOf(e.getStatus) != EnvironmentStatus.Terminated
      }.groupBy(_.getApplicationName).map { case (appName, envs) =>
        Json.obj(
          "applicationName" -> appName,
          "environments" -> Json.toJson(envs.map(Environments.envToJson))
        )
      }
    )).as("application/json").withHeaders(
      CACHE_CONTROL -> "no-cache"
    )
  }}
}
