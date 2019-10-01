import models.Spell
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions.{array_contains, size}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer

object Main extends App {

  val conf: SparkConf = new SparkConf()
    .setAppName("How to save Pito ?")
    .setMaster("local[*]")

  val sparkContext: SparkContext = new SparkContext(conf)
  sparkContext.setLogLevel("ERROR")

  println("Start crawling spells")
  val crawler: Crawler = new Crawler()
  println("End crawling spells")
  val spells: ArrayBuffer[Spell] = crawler.crawl()

  val rdd: RDD[Spell] = sparkContext.makeRDD(spells)

  val spellsToSavePito: Array[Spell] = rdd
    .collect()
    .filter(spell => spell.level.contains("sorcerer/wizard") && spell.level("sorcerer/wizard") <= 4 && spell.components.length == 1 && spell.components(0) == "V")

  println("*************************************")
  println(s"Spells to save Pito with Spark : ${spellsToSavePito.length} spells")

  spellsToSavePito.foreach(spell => println(spell.name))

  val spark: SparkSession = SparkSession.builder().getOrCreate()
  val df: DataFrame = spark.createDataFrame(rdd)

  val spellsToSavePito2: Dataset[Row] = df
    .select("name")
    .where(array_contains(df("components"), "V"))
    .where(size(df("components")) === 1)
    .where(df("level").getItem("sorcerer/wizard") <= 4)

  println("*************************************")
  println(s"Spells to save Pito with Spark SQL : ${spellsToSavePito2.count()} spells")

  spellsToSavePito2.show()

}
