package eti.italiviocorrea.ncm

import cats.effect.IO
import doobie.implicits._
import doobie.util.transactor.Transactor

class NcmServiceImpl(xa: Transactor[IO]) extends NcmService {
  override def create(ncm: Ncm) = {
    for {
      i <- NcmDao.insert(ncm).run.transact(xa)
      s <- IO {
        if (i == 1) ncm.id else "-1"
      }
    } yield s
  }

  override def update(id: String, ncm: Ncm) = {
    NcmDao.update(ncm.id, ncm.nome,ncm.inicioVigencia,ncm.fimVigencia,ncm.utribCodigo,ncm.utribDescricao).run.transact(xa)
  }

  override def getPorId(id: String) = {
    NcmDao.buscarPorId(id).option.transact(xa)
  }

  override def getTodos() = {
    NcmDao.buscarTodosPaginado(0, 10).to[List].transact(xa)
  }

}
