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
      ApiRequest.sendMessage(chatId, temp(json) + "\n" + wind(json))
    }
  }
  
  def temp(json: JValue) = {
    "Температура " + ("%1.1f" format (compact(json \ "main" \ "temp").replaceAll("\"", "").toDouble - 273,15)) + " градусов"
  }

  def wind(json: JValue) = {
    "Ветер " + windDirection(compact(json \ "wind" \ "deg").replaceAll("\"","").toDouble.toInt)
      + ", скорость " + compact(json \ "wind" \ "speed").replaceAll("\"","") + " метров в секунду"
  }

  def windDirection(degrees: Int) = {
    (degrees / 10).toInt match {
      case 0 | 1 | 2 | 34 | 35 | 36 => "северный"
      case 3 | 4 | 5 | 6 => "северо-восточный"
      case 7 | 8 | 9 | 10 | 11 => "востночный"
      case 12 | 13 | 14 | 15 => "юго-восточный"
      case 16 | 17 | 18 | 19 | 20 => "южный"
      case 21 | 22 | 23 | 24 => "юго-западный"
      case 25 | 26 | 27 | 28 | 29 => "западный"
      case 30 | 31 | 32 | 33 => "северо-западный"
      case _ => "солнечный"
    }
    
  }

  def getInfoAbout(city: String) = {
    val request: HttpRequest = Http(API_URL).param("q", city)
    logger.info("Outgoing request to weather API: " + request.toString)
    val response: HttpResponse[String] = request.asString
    logger.info("weather response: " + response.body.toString)
    parse(response.body)
  }

}
