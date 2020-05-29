package eti.italiviocorrea.ncm

import cats.effect.IO
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import doobie.hikari.HikariTransactor
import doobie.implicits._
import doobie.util.transactor.Transactor
import scala.concurrent.ExecutionContext

object Database {
  implicit val cs = IO.contextShift(ExecutionContext.global)

  def transactor(dbConfig: DbConfig): IO[HikariTransactor[IO]] = {

    // hikari config
    val config = new HikariConfig()
    config.setJdbcUrl(dbConfig.url)
    config.setDriverClassName("org.postgresql.Driver")
    config.setUsername(dbConfig.username)
    config.setPassword(dbConfig.password)
    config.setMaximumPoolSize(dbConfig.poolSize)

    // transactor with config
    val transactor: IO[HikariTransactor[IO]] =
      IO.pure(HikariTransactor.apply[IO](new HikariDataSource(config)))
    transactor
  }

  def bootstrap(xa: Transactor[IO]): IO[Int] = {
    NcmDao.createTable.run.transact(xa)
  }

}
