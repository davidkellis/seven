package davidkellis.seven.data

import javax.sql.DataSource

import davidkellis.seven.domain._
import CoreTypes.IntegerId
import com.typesafe.config.Config
import com.zaxxer.hikari.{HikariDataSource, HikariConfig}
import java.time.ZonedDateTime
import scalikejdbc._
import scalikejdbc.config.DBs

import scala.util.DynamicVariable

object Dao {
//  private val dynVar = new DynamicVariable[Dao](null)
  private val dynVar = new DynamicVariable[Dao](NoopDao)

  /**
   * Run the supplied thunk with the given Dao.
   * The Dao is stored in a thread-local variable which can be accessed with the implicit function dao.
   */
  def withDao[T](dao: Dao)(f: => T): T = dynVar.withValue(dao)(f)

  /**
   * An implicit function that returns the thread-local adapter in a withAdapater block
   */
  implicit def dao: Dao = {
    val dao = dynVar.value
//    if (dao eq NoopDao)   // formerly: if (dao eq null)
//      throw new Exception("No implicit dao has been set; dao can only be used within a withDao block")
//    else dao
    dao
  }
}

trait Dao {
  def findExchanges(exchangeLabels: Seq[String]): Seq[Exchange]
  def findSecurities(symbols: Seq[String], exchanges: Seq[Exchange]): Seq[Security]

//  def findEodBars(securityId: IntegerId, earliestTime: ZonedDateTime, latestTime: ZonedDateTime): Seq[EodBar]
//  def findMostRecentEodBar(securityId: IntegerId, time: ZonedDateTime): Option[EodBar]
//  def findMostRecentEodBarPriorTo(securityId: IntegerId, time: ZonedDateTime): Option[EodBar]
}

object NoopDao extends Dao {
  def findExchanges(exchangeLabels: Seq[String]): Seq[Exchange] = throw new Exception("Unimplemented in NoopDao.")
  def findSecurities(symbols: Seq[String], exchanges: Seq[Exchange]): Seq[Security] = throw new Exception("Unimplemented in NoopDao.")
}

// PostgresDaoForScalikeJdbc is thread-safe because the connection pool it internally uses is thread-safe
object PostgresDaoForScalikeJdbc {
  def apply(config: Config): PostgresDaoForScalikeJdbc = new PostgresDaoForScalikeJdbc(config)
}
class PostgresDaoForScalikeJdbc(config: Config) extends Dao {
  initialize(config)

  private def initialize(config: Config): Unit = {
    // If we weren't using scalikejdbc-config (see "org.scalikejdbc" %% "scalikejdbc-config"  % "2.3.4", in build.sbt),
    // we would need to first load the JDBC drivers with either:
    // Class.forName(String)
    // or
    // java.sql.DriverManager.registerDriver(java.sql.Driver)

//    useHikariConnectionPool(config)

    DBs.setupAll()
  }

  def close(): Unit = {
    DBs.closeAll()
  }

  private def setupHikariDataSource(config: Config): DataSource = {
    val hikariConfig = new HikariConfig()
    hikariConfig.setDataSourceClassName(config.getString("db.default.dataSourceClassName"))
    hikariConfig.setUsername(config.getString("db.default.user"))
    hikariConfig.setPassword(config.getString("db.default.password"))
    hikariConfig.addDataSourceProperty("serverName", config.getString("db.default.host"))
    hikariConfig.addDataSourceProperty("portNumber", config.getInt("db.default.port"))
    hikariConfig.addDataSourceProperty("databaseName", config.getString("db.default.database"))
    new HikariDataSource(hikariConfig)
  }

  private def useHikariConnectionPool(config: Config): Unit = {
    val hikariDataSource = setupHikariDataSource(config)

    // configure ScalikeJDBC connection pool to use HikariCP connection pool instead of Apache Commons DBCP 2
    ConnectionPool.singleton(new DataSourceConnectionPool(hikariDataSource))
  }

  def findExchanges(exchangeLabels: Seq[String]): Seq[Exchange] = {
    DB readOnly { implicit session =>
      sql"select * from exchanges where label in (${exchangeLabels})".
        map(toExchange(_)).list.apply()
    }
  }

  def findSecurities(symbols: Seq[String], exchanges: Seq[Exchange]): Seq[Security] = {
    DB readOnly { implicit session =>
      val exchangeIds = exchanges.map(_.id)
      sql"select * from securities where symbol in (${symbols}) and exchange_id in (${exchangeIds})".
        map(toSecurity(_)).list.apply()
    }
  }

  private def toExchange(rs: WrappedResultSet): Exchange = {
    new Exchange(
      rs.long("id"),
      rs.string("label"),
      rs.string("name"),
      rs.boolean("is_composite_exchange"),
      rs.longOpt("composite_exchange_id")
    )
  }

  private def toSecurity(rs: WrappedResultSet): Security = {
    new Security(
      rs.long("id"),
      rs.long("exchange_id"),
      rs.longOpt("security_type_id"),
      rs.longOpt("industry_id"),
      rs.longOpt("sector_id"),
      rs.string("name"),
      rs.string("symbol"),
      rs.string("figi"),
      rs.string("bbgid_composite"),
      rs.intOpt("csi_number"),
      rs.intOpt("start_date"),
      rs.intOpt("end_date")
    )
  }
}