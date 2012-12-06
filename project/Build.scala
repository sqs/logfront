import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {
  val appName         = "logfront"
  val appVersion      = "0.0.1-SNAPSHOT"

  val appDependencies = Seq()

  val main = play.Project(appName, appVersion, appDependencies).settings()
}
