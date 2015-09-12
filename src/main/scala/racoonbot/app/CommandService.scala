package racoonbot.app

import org.json4s.JsonAST.JValue
import org.slf4j.LoggerFactory
import org.json4s.jackson.JsonMethods._

class CommandService(val body: JValue) {
  val logger = LoggerFactory.getLogger(getClass)

  def run() {
    logger.info("Checking message for command presence...")
    try {
      val command = compact(body \ "message" \ "text").split(" ")(0).substring(1) // cut arguments and leading "
      val chatId = compact(body \ "message" \ "chat" \ "id")
      logger.info("command: " + command)
      command match {
        case "/start" => start(chatId)
        case "/help" => help(chatId)
        case "/weather" => weather(body)
        case _ => logger.info("Command not found.")
      }

    } catch {
      case e: Exception => logger.error("There was an error while parsing message body: " + e.printStackTrace());
    }
  }

  def start(chatId: String) = ApiRequest.sendMessage(chatId,"hi")
  def help(chatId: String) = ApiRequest.sendMessage(chatId,"Manual for racoonbot")

  def weather(body: JValue): Unit = {
    val paramsArray = compact(body \ "message" \ "text").split(" ")
    val chatId = compact(body \ "message" \ "chat" \ "id")
    if (paramsArray.length > 1) {
      val city = paramsArray(1)
      val temp = WeatherApiRequest.getTempIn(city)
      ApiRequest.sendMessage(chatId, "Погода в " + city + " ," + temp + " кельвинов" )
    } else {
      ApiRequest.sendMessage(chatId, "Укажите город")
    }
  }
}
