package racoonbot.app

import org.slf4j.LoggerFactory
import org.json4s.jackson.JsonMethods._
import scalaj.http.{HttpRequest, HttpResponse, Http}


object WeatherApiRequest {
  val API_URL = "http://api.openweathermap.org/data/2.5/weather"
  val logger = LoggerFactory.getLogger(getClass)
  
  def sendWeatherIn(city: String, chatId: String): Unit = {
    ApiRequest.sendMessage(chatId, "Укажите город")    
  }
  
  def getTempIn(city: String): Unit = {
    val request: HttpRequest = Http(API_URL).param("q", city)
    logger.info("Outgoing request to weather API: " + request.toString)
    val response: HttpResponse[String] = request.asString
    logger.info("weather response: " + response.body.toString)
    compact(parse(response.body) \ "main" \ "temp")
  }

}
