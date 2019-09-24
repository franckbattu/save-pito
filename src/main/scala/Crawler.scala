import models.Spell
import org.jsoup.Jsoup

import scala.collection.mutable.ArrayBuffer

class Crawler {

  def crawl(): ArrayBuffer[Spell] = {

    var spells = ArrayBuffer[Spell]()

    for (i <- 1 to 100) {
      println("Itération " + i)
      val document = Jsoup.connect("http://www.dxcontent.com/SDB_SpellBlock.asp?SDBID=" + i).get()
      val name = document.select(".heading p").text()
      val data = document.select(".SPDet")
      val level = this.getLevel(data.get(0).text())
      val resistance = this.getResistance(data.get(data.size() - 1).text())
      spells += new Spell(name, level, null, resistance)
    }

    spells
  }

  /**
    * Donne le level du wizard s'il est disponible, sinon le premier level trouvé
    * @param sentence
    * @return le level correct
    */
  def getLevel(sentence: String): Integer = {
    val levels: String = sentence.split("; ")(1).replace("Level ", "")
    val data: Array[String] = levels.split(" |, ")
    val index: Int = data.indexOf("sorcerer/wizard")
    if (index != -1) {
      data(index + 1).toInt
    }
    else {
      data(1).charAt(0).asDigit
    }
  }

  /**
    * Donne la résistance du wizard
    * @param sentence
    * @return true pour une résistance, false sinon
    */
  def getResistance(sentence: String): Boolean = {
    val matcher = "(?<=Spell Resistance ).*".r
    matcher.findFirstIn(sentence) match {
      case Some(value: String) => value.equals("yes")
      case None => false
    }
  }
}
