package br.eng.rafaelpsouza.trelloq

import br.eng.rafaelpsouza.trelloq.TrelloQ.hasSnapshot
import br.eng.rafaelpsouza.trelloq.extract.TrelloRestClient

/**
  * Created by Rafael Souza on 29/12/17.
  */
class TrelloQ(restClient: TrelloRestClient, databaseGateway: DatabaseGateway, queryPrint: QueryPrint){
  def takeTrelloBoardSnapshot(boardId: String): Unit = {
    databaseGateway.createSchema()
    val board = restClient.boardById(boardId)
    restClient.labelsFromBoard(boardId).foreach(label => databaseGateway.saveLabel(label))
    restClient.listsFromBoard(databaseGateway.saveBoard(board)).map(list => {
      restClient.cardsFromList(databaseGateway.saveList(list)).foreach(card => databaseGateway.saveCard(card))
    })
    hasSnapshot = true
  }

  def executeQuery(boardId: String, query: String): Unit = {
    if(!hasSnapshot){
      takeTrelloBoardSnapshot(boardId)
    }
    queryPrint.print(databaseGateway.query(query))
  }
}

object TrelloQ {

  private var hasSnapshot = false

  def apply(restClient: TrelloRestClient, databaseGateway: DatabaseGateway, queryPrint: QueryPrint): TrelloQ = {
    new TrelloQ(restClient, databaseGateway, queryPrint)
  }

}
