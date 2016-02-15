package com.github.davidkellis.seven

import java.io.File

import com.github.davidkellis.seven.data.{PostgresDaoForScalikeJdbc, Dao}
import com.github.davidkellis.seven.strategies.BuyAndHold
import com.typesafe.config.{ConfigFactory, Config}
import net.sf.ehcache.CacheManager
import org.joda.time.DateTime
import org.rogach.scallop.ScallopConf

object Main {
  class RuntimeArgs(arguments: Seq[String]) extends ScallopConf(arguments) {
    version("seven 1.0.0")
    banner("""Usage: seven [--config <file-path = ./config/application.conf>] [--scenario <scenarioName>]
             |Options:
             |""".stripMargin)

    val configPath = opt[String](descr = "Specify the path to the ", short = 'c', default = Some("./config/application.conf"))
    val scenario = opt[String](descr = "Identify the scenario to run.", short = 's')
  }

  def main(args: Array[String]) {
    // extract runtime args
    val runtimeArgs = new RuntimeArgs(args)
    val configPath = runtimeArgs.configPath()

    // extract settings from config file
    val config = loadConfig(configPath)
    val logLevel = config.getString("log_level")
    val dbLogLevel = config.getString("database_log_level")

    val dao = PostgresDaoForScalikeJdbc(config)
    Dao.withDao(dao) {
      run(runtimeArgs)
    }
    dao.close()

    cleanupCache()
  }

  def loadConfig(configPath: String): Config = {
    val configFile = new File(configPath)
    if (configFile.exists()) {
      ConfigFactory.parseFile(configFile)
    } else {
      ConfigFactory.load()
    }
  }

  def run(runtimeArgs: RuntimeArgs): Unit = {
    val scenario = runtimeArgs.scenario.get

    if (scenario.isDefined) {
      val t1 = DateTime.now()
      println(s"Starting scenario at ${t1}")
      scenario match {
        case Some("bh1") => BuyAndHold.Scenarios.runSingleBuyAndHoldTrial()
        //        case Some(name) => println(s"Unknown scenario: $name")
        case _ => println("No scenario was specified.")
      }
      val t2 = DateTime.now()
      println(s"Finished scenario at ${t2}; Total time was ${Time.periodBetween(t1, t2)}")
    }
  }

  def cleanupCache() {
    CacheManager.getInstance().shutdown()
  }
}
