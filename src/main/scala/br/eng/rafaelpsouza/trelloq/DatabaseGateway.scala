package br.eng.rafaelpsouza.trelloq

import br.eng.rafaelpsouza.trelloq.extract.{Board, Card, Label, TrelloList}

/**
  * Created by Rafael Souza on 29/12/17.
  */
trait DatabaseGateway {

  def createSchema(): Unit

  def saveBoard(board: Board):String

  def saveList(list: TrelloList): String

  def saveLabel(label: Label): String

  def saveCard(card: Card): String

  def query(query: String): QueryResult

}
