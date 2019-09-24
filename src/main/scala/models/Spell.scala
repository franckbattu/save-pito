package models

class Spell(name: String, level: Int, components: List[String], resistance: Boolean) {

  override def toString = s"Spell(name=${this.name}, level=${this.level}, components=${this.components}, resistance=${this.resistance})"

}
