package eti.italiviocorrea.ncm

import java.util.Date

import doobie.implicits._

object NcmDao {

  def createDb = {
    sql"""
         |CREATE DATABASE IF NOT EXISTS ncmdb
       """
      .update
  }

  def createTable = {
    sql"""
         |CREATE TABLE IF NOT EXISTS ncms (
         |  id VARCHAR(8) PRIMARY KEY,
         |  name VARCHAR(256),
         |  inicioVigencia DATE ,
         |  fimVigencia DATE ,
         |  uTribCodigo VARCHAR(10),
         |  uTribDescricao VARCHAR(20)
         |)
       """.stripMargin
      .update
  }

  def insert(ncm: Ncm): doobie.Update0 = {
    sql"""
         |INSERT INTO ncms (
         |  id,
         |  nome,
         |  inicioVigencia,
         |  fimVigencia,
         |  uTribCodigo,
         |  uTribDescricao
         |)
         |VALUES (
         |  ${ncm.id},
         |  ${ncm.nome},
         |  ${ncm.inicioVigencia},
         |  ${ncm.fimVigencia},
         |  ${ncm.uTribCodigo},
         |  ${ncm.uTribDescricao}
         |)
        """.stripMargin
      .update
  }

  // update query
  def update(id: String, nome: String, inicioVigencia: Date, fimVigencia: Date, uTribCodigo: String, uTribDescricao: String): doobie.Update0 = {
    sql"""
         |UPDATE ncms
         |SET nome = $nome,
         |    inicioVigencia = $inicioVigencia,
         |    fimVigencia = $fimVigencia,
         |    uTribCodigo = $uTribCodigo,
         |    uTribDescricao = $uTribDescricao
         |WHERE id = $id
       """.stripMargin
      .update
  }

  // delete query
  def delete(id: String): doobie.Update0 = {
    sql"""
         |DELETE FROM ncms
         |WHERE id=$id
       """.stripMargin
      .update
  }

  // search range
  def buscarTodosPaginado(offset: Int, limit: Int): doobie.Query0[Ncm] = {
    sql"""
         |SELECT * FROM ncms
         |LIMIT $limit
         |OFFSET $offset
       """.stripMargin
      .query[Ncm]
  }

  // search with id
  def buscarPorId(id: String): doobie.Query0[Ncm] = {
    sql"""
         |SELECT * FROM ncms
         |WHERE id = $id
         |LIMIT 1
       """.stripMargin
      .query[Ncm]
  }

}
