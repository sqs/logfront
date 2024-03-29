import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {
  val appName         = "logfront"
  val appVersion      = "0.0.1-SNAPSHOT"

  val appDependencies = Seq(
    "com.amazonaws" % "aws-java-sdk" % "1.3.26",
    "net.schmizz" % "sshj" % "0.8.1",
    "org.bouncycastle" % "bcprov-jdk16" % "1.46"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalaVersion := "2.10.0-RC3"
  )
}
