package logfront.controllers

import logfront.AWS

trait AWSClient {
  val ec2Client = AWS.ec2Client
  val elasticBeanstalkClient = AWS.elasticBeanstalkClient

  def throttled[T](block: => T): T = synchronized {
    play.api.Logger.debug(s"AWSClient throttled { ... }")
    java.lang.Thread.sleep(500)
    block
  }
}
