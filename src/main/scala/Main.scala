import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object Main extends App {

  val conf = new SparkConf()
    .setAppName("How to save Pito ?")
    .setMaster("local[*]")

  val sparkContext = new SparkContext(conf)
  sparkContext.setLogLevel("ERROR")

  val crawler = new Crawler()
  val spells = crawler.crawl()

  spells.foreach(spell => println(spell))


  // val rdd = sparkContext.makeRDD(spells)

}
