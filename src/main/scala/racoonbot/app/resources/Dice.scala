package racoonbot.app.resources

import scalaj.http.Http

object Dice {
  def get(num: Int): Array[Byte] = {
    Http("http://faculty.purchase.edu/jeanine.meyer/dice" + num + ".gif").asBytes.body
  }
}
