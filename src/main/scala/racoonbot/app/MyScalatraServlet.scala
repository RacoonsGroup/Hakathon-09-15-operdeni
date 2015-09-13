package racoonbot.app

import org.json4s.{DefaultFormats, Formats, JValue}
import org.scalatra.json.JacksonJsonSupport
import org.slf4j.LoggerFactory

class MyScalatraServlet extends RacoonbotStack with JacksonJsonSupport {

  protected implicit lazy val jsonFormats: Formats = DefaultFormats
  protected override def transformRequestBody(body: JValue): JValue = body.camelizeKeys
  protected override def transformResponseBody(body: JValue): JValue = body.underscoreKeys

  val logger =  LoggerFactory.getLogger(getClass)

  before() {
    contentType = formats("json")
  }

  get("/") {
    logger.info("GET /")
    contentType="text/html"
    jade("index")
  }

  post("/133163653:AAEIPp8IA7xgxh2dqUlWTX3W90RbSS5kJRU") {
    logger.info("Incoming request: " + parsedBody)
    new CommandService(parsedBody).run
  }

}
