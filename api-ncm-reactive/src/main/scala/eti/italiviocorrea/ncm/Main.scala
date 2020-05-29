package eti.italiviocorrea.ncm

import cats.data.Kleisli
import cats.effect.{ExitCode, IO, IOApp}
import doobie.util.transactor.Transactor
import fs2.Stream
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.{Request, Response}


object Main extends IOApp {

  def makeRouter(transactor: Transactor[IO]): Kleisli[IO, Request[IO], Response[IO]] = {
    Router[IO](
      "/dfe/v1" -> NcmRoutes.routes(new NcmServiceImpl(transactor))
    ).orNotFound
  }

  def serveStream(transactor: Transactor[IO], serverConfig: ServerConfig): Stream[IO, ExitCode] = {
    BlazeServerBuilder[IO]
      .bindHttp(serverConfig.port, serverConfig.host)
      .withHttpApp(makeRouter(transactor))
      .serve
  }

  override def run(args: List[String]): IO[ExitCode] = {
    val stream = for {
      config <- Stream.eval(Config.load())
      xa <- Stream.eval(Database.transactor(config.dbConfig))
      _ <- Stream.eval(Database.bootstrap(xa))
      exitCode <- serveStream(xa, config.serverConfig)
    } yield exitCode

    stream.compile.drain.as(ExitCode.Success)
  }

}
