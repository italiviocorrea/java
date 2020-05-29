package eti.italiviocorrea.ncm

import cats.effect.IO

trait NcmService {
  def create(ncm: Ncm): IO[String]

  def update(id: String, ncm: Ncm): IO[Int]

  def getPorId(id: String): IO[Option[Ncm]]

  def getTodos(): IO[List[Ncm]]

}
