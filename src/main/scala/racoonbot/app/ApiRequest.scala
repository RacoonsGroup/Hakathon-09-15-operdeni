package racoonbot.app

import org.slf4j.LoggerFactory

import scalaj.http.{HttpResponse, Http}


class ApiRequest {
  val API_URL = "https://api.telegram.org/bot133163653:AAEIPp8IA7xgxh2dqUlWTX3W90RbSS5kJRU/"
  val logger =  LoggerFactory.getLogger(getClass)
//  def perform(method: String): Unit = {
//    Http(API_URL + method)
//      .param("q", "monkeys").asString
//  }

  def sendMessage(chatId: String, text: String): Unit = {
    val response: HttpResponse[String] = Http(API_URL + "sendMessage").param("chat_id", chatId.toString).param("text", text).asString
    logger.info("Telegram response: " + response.toString)
  }
}
