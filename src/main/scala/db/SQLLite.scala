package db

import java.sql.{Connection, DriverManager, ResultSet, Statement}

import models.Spell

import scala.collection.mutable.ArrayBuffer

class SQLLite {

  private var connection: Connection = _
  private var statement: Statement = _
  this.init()

  def init(): Unit = {
    Class.forName("org.sqlite.JDBC")
    val url: String = "jdbc:sqlite::memory:"
    this.connection = DriverManager.getConnection(url)
    this.statement = connection.createStatement()
    this.statement.executeUpdate("DROP TABLE IF EXISTS spells")
    this.statement.executeUpdate("CREATE TABLE spells (id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, components STRING, level INTEGER, resistance BOOLEAN)")
  }

  /**
    * Liste les spells capable de sauver Pito
    */
  def savePito(): Unit = {
    val rs: ResultSet = this.statement.executeQuery("SELECT * FROM spells WHERE level <= 4 AND components = '[[\"V\"]]'")
    while (rs.next()) {
      println(rs.getString("name"))
    }
  }

  /**
    * Insère tous les spells dans la base de données
    * @param spells ArrayBuffer[Spell] la liste des spells à insérer
    */
  def insertMany(spells: ArrayBuffer[Spell]): Unit = {
    for (spell <- spells) {
      val json = ujson.Arr(spell.components)
      val req = "INSERT INTO spells (name, components, level, resistance) VALUES(?, ?, ?, ?)"
      val preparedStatement = this.connection.prepareStatement(req)
      preparedStatement.setString(1, spell.name)
      preparedStatement.setString(2, json.toString())
      preparedStatement.setInt(3, spell.level.getOrElse("sorcerer/wizard", 5))
      preparedStatement.setBoolean(4, spell.resistance)
      preparedStatement.executeUpdate()
    }
  }

}
