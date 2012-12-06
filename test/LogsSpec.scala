package logfront.test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

class LogsSpec extends Specification {  
  "Logs controller" should {
    "return logs for an application" in {
      running(FakeApplication()) {
        val index = route(FakeRequest(GET, "/api/hosts/example.com/logs/main")).get
        
        status(index) must equalTo(OK)
        contentType(index) must beSome.which(_ == "application/json")
        header("Cache-Control", index) must beSome.which(_ == "no-cache")
      }
    }
  }
}
