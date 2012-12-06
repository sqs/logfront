package logfront.controllers

import com.amazonaws.services.elasticbeanstalk.model._
import play.api._
import play.api.libs.json._
import play.api.mvc._
import scala.collection.JavaConversions._

object Environments extends Controller with AWSClient {
  def index(appName: String) = Action { throttled {
    Ok(Json.toJson(
      elasticBeanstalkClient.describeEnvironments(
        new DescribeEnvironmentsRequest().withApplicationName(appName)
      ).getEnvironments.filter { e =>
        EnvironmentStatus.valueOf(e.getStatus) != EnvironmentStatus.Terminated
      }.map(envToJson)
    )).as("application/json").withHeaders(
      CACHE_CONTROL -> "no-cache"
    )
  }}

  def get(appName: String, envName: String) = Action { throttled {
    elasticBeanstalkClient.describeEnvironments(
      new DescribeEnvironmentsRequest().withEnvironmentNames(List(envName))
    ).getEnvironments.headOption.map { env =>
      Ok(envToJson(env)).as("application/json").withHeaders(
        CACHE_CONTROL -> "no-cache"
      )
    }.getOrElse { NotFound }
  }}

  def envToJson(env: EnvironmentDescription) =
    Json.obj(
      "environmentName" -> env.getEnvironmentName,
      "environmentId" -> env.getEnvironmentId,
      "CNAME" -> env.getCNAME,
      "_isActive" -> !env.getCNAME.contains("-jen"),
      "endpointURL" -> env.getEndpointURL,
      "health" -> env.getHealth,
      "status" -> env.getStatus
    )
}
