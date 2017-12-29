package br.eng.rafaelpsouza.trelloq

import java.sql.ResultSet

/**
  * Created by Rafael Souza on 27/12/17.
  */
trait Query {

  //@TODO create own output representation to decouple from ResultSet
  def query(query: String): QueryResult

}

case class QueryResult(header: List[String], values: List[Map[String, Any]])
