package racoonbot.app

import org.json4s.JsonAST.JValue
import org.slf4j.LoggerFactory
import org.json4s.jackson.JsonMethods._

class CommandService(val body: JValue) {
  val logger =  LoggerFactory.getLogger(getClass)
  val chatId = compact(body \ "message" \ "chat" \ "id")

  def run = {
  	help(chatId)
  }

  def start(chatId: String) = ApiRequest.sendMessage(chatId,"hi")
  def help(chatId: String) = ApiRequest.sendMessage(chatId,"Manual for racoonbot")
}


