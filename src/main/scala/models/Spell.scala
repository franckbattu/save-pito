package models

import scala.collection.mutable.ArrayBuffer

case class Spell(var name: String, var level: Map[String, Int], var components: ArrayBuffer[String], var resistance: Boolean) extends Serializable {

  override def toString = s"Spell(name=${this.name}, level=${this.level}, components=${this.components}, resistance=${this.resistance})"

}
