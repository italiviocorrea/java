package eti.italiviocorrea.ncm

import cats.effect.IO
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl

object NcmRoutes {
  /**
   * Definine a APIS :
   *   1. criar um ncm — POST dfe/v1/ncms
   *   2. atualizar um ncm — PUT dfe/v1/ncms/{id}
   *   3. buscar por id um ncm — GET dfe/v1/ncms/{id}
   *   4. buscar todos ncms — GET dfe/v1/ncms
   */
  def routes(documentRepo: NcmService): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._

    HttpRoutes.of[IO] {
      case req@POST -> Root / "ncms" =>
        req.decode[Ncm] { acc =>
          documentRepo.create(acc).flatMap(id => Created(id))
        }.handleErrorWith(e => BadRequest(e.getMessage))
      case req@PUT -> Root / "ncms" / id =>
        req.decode[Ncm] { acc =>
          documentRepo.update(id, acc).flatMap(_ => Accepted())
        }.handleErrorWith(e => BadRequest(e.getMessage))
      case _@GET -> Root / "ncms" =>
        documentRepo.getTodos().flatMap(accs => Ok(accs))
      case _@GET -> Root / "ncms" / id =>
        documentRepo.getPorId(id) flatMap {
          case None => NotFound()
          case Some(acc) => Ok(acc)
        }
    }
  }

}
