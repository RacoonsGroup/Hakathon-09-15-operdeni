package racoonbot.app

import org.json4s.JsonAST.JValue
import org.slf4j.LoggerFactory
import org.json4s.jackson.JsonMethods._

class CommandService(val body: JValue) {
  val logger =  LoggerFactory.getLogger(getClass)

  def start(): Unit = {

  }

  def help(): Unit = {

  }

  def moo(): Unit = {
    val chatId = compact(body \ "message" \ "chat" \ "id")
    ApiRequest.sendMessage(chatId, "There are no Easter Eggs in this program.")
  }
}
