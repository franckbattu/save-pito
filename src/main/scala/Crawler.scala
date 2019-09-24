import models.Spell
import org.jsoup.Jsoup

import scala.collection.mutable.ArrayBuffer

class Crawler {

  def crawl(): ArrayBuffer[Spell] = {

    var spells = ArrayBuffer[Spell]()

    for (i <- 1 to 1600) {
      println("Itération " + i)
      val document = Jsoup.connect("http://www.dxcontent.com/SDB_SpellBlock.asp?SDBID=" + i).get()
      val name = document.select(".heading p").text()
      val data = document.select(".SPDet")
      val level = this.getLevel(data.get(0).text())
      val components = this.getComponents(data.get(2).text())
      val resistance = this.getResistance(data.get(data.size() - 1).text())
      spells += new Spell(name, level, components, resistance)
    }

    spells
  }

  /**
    * Donne le level du wizard s'il est disponible, sinon le premier level trouvé
    * @param sentence String
    * @return Int
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
    * @param sentence String
    * @return Boolean
    */
  def getResistance(sentence: String): Boolean = {
    val matcher = "(?<=Spell Resistance ).*".r
    matcher.findFirstIn(sentence) match {
      case Some(value: String) => value.equals("yes")
      case None => false
    }
  }

  /**
    * Donne les components de chaque sort
    * @param sentence String
    * @return ArrayBuffer[String]
    */
  def getComponents(sentence: String): ArrayBuffer[String] = {
    var result = new ArrayBuffer[String]()
    val matcher = "[A-Z]+".r
    for (component <- matcher.findAllIn(sentence)) {
      if (!component.equals("C")) {
        result += component
      }
    }

    result
  }
}
