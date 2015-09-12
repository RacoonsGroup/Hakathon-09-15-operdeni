package racoonbot.app

import org.json4s.JsonAST.JValue
import org.json4s.jackson.JsonMethods._

object MessageParser {
  def parse(body: JValue): Unit = {
    val service = new CommandService(body)
    val text = compact(body \ "message" \ "text")
    if (text.toString.startsWith("/")) {
      val commandName = text.toString
      commandName match {
        case "/start" => service.start()
        case _ => service.help()
      }
    } else {
      service.help()
    }
  }
}
