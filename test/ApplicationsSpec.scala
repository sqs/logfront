package logfront.test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

class ApplicationsSpec extends Specification {  
  "Applications controller" should {
    "return all applications" in {
      running(FakeApplication()) {
        val index = route(FakeRequest(GET, "/api/applications")).get
        
        status(index) must equalTo(OK)
        contentType(index) must beSome.which(_ == "application/json")
        header("Cache-Control", index) must beSome.which(_ == "no-cache")
      }
    }
  }
}
