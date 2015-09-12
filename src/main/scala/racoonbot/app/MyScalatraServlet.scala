package racoonbot.app

import org.json4s.{JValue, DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport
import org.slf4j.LoggerFactory

case class User(id: Int, firstName: String)

class MyScalatraServlet extends RacoonbotStack with JacksonJsonSupport {

  protected implicit lazy val jsonFormats: Formats = DefaultFormats
  protected override def transformRequestBody(body: JValue): JValue = body.camelizeKeys
  protected override def transformResponseBody(body: JValue): JValue = body.underscoreKeys

  val logger =  LoggerFactory.getLogger(getClass)

  before() {
    contentType = formats("json")
  }

  get("/")("It works!")

  post("/133163653:AAEIPp8IA7xgxh2dqUlWTX3W90RbSS5kJRU") {
    logger.info("Incoming request: " + request.body)
    <html><body>lorem ipsum<br/></body></html>
  }

}