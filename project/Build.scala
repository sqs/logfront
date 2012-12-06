import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {
  val appName         = "logfront"
  val appVersion      = "0.0.1-SNAPSHOT"

  val appDependencies = Seq(
    "com.amazonaws" % "aws-java-sdk" % "1.3.26"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalaVersion := "2.10.0-RC3"
  )
}
