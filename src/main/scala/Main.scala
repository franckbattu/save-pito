import models.Spell
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

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
    .filter(spell => spell.level <= 4 && spell.components.length == 1 && spell.components(0) == "V")

  println("Spells to save Pito :")
  spellsToSavePito.foreach(spell => println(spell))

}
