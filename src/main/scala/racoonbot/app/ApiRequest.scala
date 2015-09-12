package racoonbot.app

import scalaj.http.Http


class ApiRequest {
  val API_URL = "https://api.telegram.org/bot133163653:AAEIPp8IA7xgxh2dqUlWTX3W90RbSS5kJRU/"

  def perform(method: String, args: Map[String,String]): Unit = {
    Http(API_URL + method)
      .param("q", "monkeys").asString
  }

  def sendMessage(chatId: Int, text: String): Unit = {
    Http(API_URL + "sendMessage").param("chat_id", chatId.toString).param("text", text).asString
  }
}
