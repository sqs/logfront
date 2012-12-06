package logfront.controllers

import logfront.AWS

trait AWSClient {
  val elasticBeanstalkClient = AWS.elasticBeanstalkClient
}
