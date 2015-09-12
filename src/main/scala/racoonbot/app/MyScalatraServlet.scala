package racoonbot.app

import org.json4s.JsonAST.JValue
import org.json4s.{Formats, DefaultFormats}
import org.scalatra.json.JacksonJsonSupport
import org.slf4j.LoggerFactory


class MyScalatraServlet extends RacoonbotStack with JacksonJsonSupport {

  protected implicit lazy val jsonFormats: Formats = DefaultFormats
  protected override def transformResponseBody(body: JValue): JValue = body.underscoreKeys

  val logger =  LoggerFactory.getLogger(getClass)

  before() {
    contentType = formats("json")
  }

  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
        Say <a href="hello-scalate">hello to Scalate</a>.
      </body>
    </html>
  }

  post("/133163653:AAEIPp8IA7xgxh2dqUlWTX3W90RbSS5kJRU") {
    logger.info("Incoming request: " + request.body.toString)
    <html><body>lorem ipsum</body></html>
  }

}
