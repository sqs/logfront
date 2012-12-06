package logfront

import com.amazonaws.auth.PropertiesCredentials
import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalkClient
import com.amazonaws.services.ec2.AmazonEC2Client
import java.io.File
import play.api.Play

object AWS {
  private lazy val awsCredentials = {
    val file = new File(new File(System.getenv("HOME")), ".aws-credentials")
    if (!file.exists) {
      throw new Exception("AWS credentials file not found at " + file.getAbsolutePath + "\n\n" +
                          "Create a file at that path with the following contents:\n\n" +
                          "accessKey = <your AWS access key>\n" +
                          "secretKey = <your AWS secret key>\n")
    }
    new PropertiesCredentials(file)
  }

  private lazy val region = Play.current.configuration.getString("aws.region").getOrElse {
    throw new Exception("The `aws.region` must be specified in the logfront configuration.")
  }

  lazy val ec2Client: AmazonEC2Client = {
    val c = new AmazonEC2Client(awsCredentials)
    c.setEndpoint("https://ec2." + region + ".amazonaws.com")
    c
  }

  lazy val elasticBeanstalkClient: AWSElasticBeanstalkClient = {
    val c = new AWSElasticBeanstalkClient(awsCredentials)
    c.setEndpoint("https://elasticbeanstalk." + region + ".amazonaws.com")
    c
  }
}
