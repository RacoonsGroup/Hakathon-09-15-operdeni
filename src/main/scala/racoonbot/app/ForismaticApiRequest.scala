package racoonbot.app

import org.slf4j.LoggerFactory

import scalaj.http.{Http, HttpRequest, HttpResponse}

object ForismaticApiRequest {
  val API_URL = "http://api.forismatic.com/api/1.0/"
  val logger = LoggerFactory.getLogger(getClass)

  def getRandomQuote: String = {
    val request: HttpRequest = Http(API_URL).param("method", "getQuote").param("format", "text").param("lang", "ru")
    logger.info("Outgoing request to forismatic API: " + request.toString)
    val response: HttpResponse[String] = request.asString
    logger.info("Forismatic response: " + response.body)
    response.body
  }
}
