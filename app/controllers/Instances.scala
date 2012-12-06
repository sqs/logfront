package logfront.controllers

import com.amazonaws.services.elasticbeanstalk.model._
import play.api._
import play.api.libs.json._
import play.api.mvc._
import scala.collection.JavaConversions._

object Instances extends Controller with AWSClient {
  def index(appName: String, envName: String) = Action {
    Ok(Json.toJson(
      elasticBeanstalkClient.describeEnvironmentResources(
        new DescribeEnvironmentResourcesRequest()
        .withEnvironmentName(envName)
      ).getEnvironmentResources.getInstances.map(instanceToJson)
    )).as("application/json").withHeaders(
      CACHE_CONTROL -> "no-cache"
    )
  }

  private def instanceToJson(inst: Instance) =
    Json.obj(
      "id" -> inst.getId
    )
}
