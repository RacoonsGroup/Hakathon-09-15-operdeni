package racoonbot.app

import org.json4s.JsonAST.JValue
import org.json4s.jackson.JsonMethods._
import org.slf4j.LoggerFactory
import racoonbot.app.resources.{Dice, DevAnswer}

import scalaj.http.{Http, HttpRequest}

class CommandService(val body: JValue) {
  val logger = LoggerFactory.getLogger(getClass)

  def run() {
    logger.info("Checking message for command presence...")
    try {
      val command = compact(body \ "message" \ "text").split(" ")(0).replaceAll("\"", "") // cut arguments and leading "
      val chatId = compact(body \ "message" \ "chat" \ "id")
      logger.info("command: " + command)
      command match {
        case "/start" => start(chatId)
        case "/help" => help(chatId)
        case "/weather" => weather(body)
        case "/moo" => moo(chatId)
        case "/devanswer" => devAnswer(chatId)
        case "/quote" => randomQuote(chatId)
        case "/dice" => rollDice(body, chatId)
        case "/image" => sendImage(body, chatId)
        case _ => logger.info("Command not found.")
      }

    } catch {
      case e: Exception => logger.error("There was an error while parsing message body: " + e.printStackTrace());
    }
  }

  def start(chatId: String) = ApiRequest.sendMessage(chatId,"Привет!")
  def help(chatId: String): Unit = {
    ApiRequest.sendMessage(chatId,"help! \n  /weather <city> \n  /dice <dice quantity 1..4> \n  /devanswer \n  /quote \n  /image")
  }

  def weather(body: JValue): Unit = {
    val paramsArray = compact(body \ "message" \ "text").split(" ")
    val chatId = compact(body \ "message" \ "chat" \ "id")
    if (paramsArray.length > 1) {
      val city = paramsArray(1).replaceAll("\"", "")
      if (city == "Простоквашино") {
        ApiRequest.sendMessage(chatId, "Ожидаются заморозки, переходящие в наводнение. Ожидается землетрясение, переходящее в солнечное затмение. Местами снег, местами град, местами кислый виноград.")
      } else {
        WeatherApiRequest.showWeatherIn(city, chatId)
      }
    } else {
      ApiRequest.sendMessage(chatId, "Укажите город")
    }
  }

  def moo(chatId: String) = ApiRequest.sendMessage(chatId,"There are no Easter Eggs in this program.")

  def devAnswer(chatId: String): Unit = {
    val quote = DevAnswer.random()
    ApiRequest.sendMessage(chatId, quote)
  }

  def randomQuote(chatId: String): Unit = {
    val quote = ForismaticApiRequest.getRandomQuote
    ApiRequest.sendMessage(chatId, quote)
  }

  def roll(): Int  = scala.util.Random.nextInt(6) + 1

  def rollDice(body: JValue, chatId: String): Unit = {
    logger.info("Parsing dice number...")
    try {
      val diceNum = compact(body \ "message" \ "text").split(" ")(1).replaceAll("\"", "").toInt
      val user = (compact(body \ "message" \ "from" \ "firstName") + " " + compact(body \ "message" \ "from" \ "lastName")).replaceAll("\"", "")
      if (diceNum <= 0) {
        ApiRequest.sendMessage(chatId, "Нечего бросать.")
      } else if (diceNum >= 5) {
        ApiRequest.sendMessage(chatId, "Слишком много кубиков.")
      } else {
        var i = 0
        var overall = 0
        val text = user + " бросает " + diceNum + " " + (if (diceNum == 1) "кубик" else "кубика") + "..."
        ApiRequest.sendMessage(chatId, text)
        for( i <- 1 to diceNum) {
          val result = roll()
          ApiRequest.sendPhoto(chatId, Dice.get(result), "image/gif", result.toString + ".gif")
          overall += result
        }
        ApiRequest.sendMessage(chatId, "\n" + user + " выбрасывает: " + overall.toString)
      }

    } catch {
      case e: Exception =>
        logger.error("There was an error while parsing message body: " + e.printStackTrace())
        ApiRequest.sendMessage(chatId, "Нечего бросать.");
    }

  }

  def sendImage(body: JValue, chatId: String) = {
    val imageName = bodyCommand(body, 1).toString
    if (imageName == "false") {
      ApiRequest.sendMessage(chatId, "Укажите картинку")
    } else {
      val imageSearch = "https://ajax.googleapis.com/ajax/services/search/images"

      val request: HttpRequest = Http(imageSearch).param("v", "1.0").param("as_filetype", "jpg").param("q", imageName)
      val result = parse(request.asString.body)
      logger.info("google response: " + result.toString)
      if (compact(result \ "responseStatus") == "200") {
        val imageRequest: HttpRequest = Http(compact((result \ "responseData" \ "results")(0) \ "unescapedUrl").replaceAll("\"", ""))
        val image = imageRequest.asBytes.body
        logger.info("image bytecode: " + image.toString)
        ApiRequest.sendPhoto(chatId, image, "image/jpg", (imageName + ".jpg").toString)
      } else {
        ApiRequest.sendMessage(chatId, "Ошибка при поиске картинки 😥")
      }

    }
  }

  def bodyCommand(body: JValue, number: Int) = {
    val array = compact(body \ "message" \ "text").split(" ")
    if (array.length >= number) {
      array(number).replaceAll("\"", "")
    } else {
      false
    }

  }
}
