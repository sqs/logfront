package logfront.controllers

import com.amazonaws.services.ec2.{ model => ec2 }
import com.amazonaws.services.elasticbeanstalk.{ model => eb }
import play.api._
import play.api.libs.json._
import play.api.mvc._
import scala.collection.JavaConversions._

object Instances extends Controller with AWSClient {
  def index(appName: String, envName: String) = Action {
    Ok(Json.toJson(
      elasticBeanstalkClient.describeEnvironmentResources(
        new eb.DescribeEnvironmentResourcesRequest()
        .withEnvironmentName(envName)
      ).getEnvironmentResources.getInstances.map(instanceToJson)
    )).as("application/json").withHeaders(
      CACHE_CONTROL -> "no-cache"
    )
  }

  def get(appName: String, envName: String, instanceId: String) = Action {
    ec2Client.describeInstances(
      new ec2.DescribeInstancesRequest()
      .withInstanceIds(Set(instanceId))
    ).getReservations.flatMap(_.getInstances).headOption.map { inst =>
      Ok(Json.toJson(instanceToJson(inst))).as("application/json").withHeaders(
        CACHE_CONTROL -> "no-cache"
      )
    } getOrElse { NotFound }
  }

  private def instanceToJson(inst: eb.Instance) =
    Json.obj(
      "id" -> inst.getId
    )

  private def instanceToJson(inst: ec2.Instance) =
    Json.obj(
      "id" -> inst.getInstanceId,
      "instanceType" -> inst.getInstanceType,
      "keyName" -> inst.getKeyName,
      "name" -> inst.getTags.find(_.getKey == "Name").map(_.getValue).getOrElse("").toString,
      "privateIpAddress" -> inst.getPrivateIpAddress
    )
}
