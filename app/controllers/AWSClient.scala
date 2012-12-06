package logfront.controllers

import logfront.AWS

trait AWSClient {
  val ec2Client = AWS.ec2Client
  val elasticBeanstalkClient = AWS.elasticBeanstalkClient
}
