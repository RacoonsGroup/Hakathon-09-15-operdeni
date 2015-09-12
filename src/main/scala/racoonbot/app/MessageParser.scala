package racoonbot.app

import org.json4s.JsonAST.JValue
import org.json4s.jackson.JsonMethods._
//case class User(id: Int, firstName: String)

class MessageParser {
  val service = new CommandService

  def parse(body: JValue): Unit = {
    val text = compact(body \ "message" \ "text")
    if (text.toString.startsWith("/")) {
      val commandName = text.toString
      commandName match {
        case "/start" => service.start(body)
        case _ => service.help(body)
      }
    } else {
      service.help(body)
    }
  }
}
