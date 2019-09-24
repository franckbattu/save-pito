package models

import scala.collection.mutable.ArrayBuffer

class Spell(name: String, level: Int, components: ArrayBuffer[String], resistance: Boolean) {

  override def toString = s"Spell(name=${this.name}, level=${this.level}, components=${this.components}, resistance=${this.resistance})"

}
