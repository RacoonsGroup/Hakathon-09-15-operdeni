package racoonbot.app

import org.scalatra.test.specs2._
import org.specs2.mock.Mockito
import org.specs2.specification.BeforeEach

// For more on Specs2, see http://scalatra.org/2.4/guides/testing/specs2.html
class MyScalatraServletSpec extends ScalatraSpec with Mockito with BeforeEach {
  def is = sequential ^ s2"""
  MyScalatraServlet

  GET root
    must return status 200                          $get_root200

  POST web hook url
  |must return status 200                           $post_webHookUrl200
  """

  lazy val commandServiceMock = mock[CommandService]
  private final val WEB_HOOK_URL = "/133163653:AAEIPp8IA7xgxh2dqUlWTX3W90RbSS5kJRU"

  def exampleJson(): Array[Byte] = {
    val json = """{"updateId":959200318, "message":{"from":"test", "chat":{"id": 126424064}}}"""
    json.getBytes
  }

  addServlet(classOf[MyScalatraServlet], "/*")

  def before = {
    doNothing.when(commandServiceMock).run()
  }

  def get_root200 = get("/"){
    status must_== 200
  }

  def post_webHookUrl200 = post(WEB_HOOK_URL, exampleJson(), Map("Content-Type"->"application/json")) {
    status must_== 200
  }
}
