package racoonbot.app

import org.json4s.JsonAST.JValue
import org.json4s.jackson.JsonMethods._

object MessageParser {
  def parse(body: JValue): Unit = {
    val text = compact(body \ "message" \ "text")
    if (text.toString.startsWith("/")) {
      val commandName = text.toString
      commandName match {
        case "/start" => CommandService.start(body)
        case _ => CommandService.help(body)
      }
    } else {
      CommandService.help(body)
    }
  }
}
