package racoonbot.app

import org.slf4j.LoggerFactory
import org.json4s.JsonAST.JValue
import org.json4s.jackson.JsonMethods._
import scalaj.http.{HttpRequest, HttpResponse, Http}

object WeatherApiRequest {
  val API_URL = "http://api.openweathermap.org/data/2.5/weather"
  val logger = LoggerFactory.getLogger(getClass)
  
  def showWeatherIn(city: String, chatId: String) = {
    val json = getInfoAbout(city)
    if (compact(json \ "cod").replaceAll("\"", "") == "404"){
      ApiRequest.sendMessage(chatId, "Город не найден :'(")
    } else {
      ApiRequest.sendMessage(chatId, "Температура " + temp(json) + "градусов")
    }
  }
  
  def temp(json: JValue) = {
    compact(json \ "main" \ "temp").replaceAll("\"", "")
  }

  def getInfoAbout(city: String) = {
    val request: HttpRequest = Http(API_URL).param("q", city)
    logger.info("Outgoing request to weather API: " + request.toString)
    val response: HttpResponse[String] = request.asString
    logger.info("weather response: " + response.body.toString)
    parse(response.body)
  }

}
