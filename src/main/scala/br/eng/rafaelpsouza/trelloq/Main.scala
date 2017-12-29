package br.eng.rafaelpsouza.trelloq

import br.eng.rafaelpsouza.trelloq.database.H2DatabaseGateway
import br.eng.rafaelpsouza.trelloq.extract.TrelloRestClient
import br.eng.rafaelpsouza.trelloq.print.ConsoleTablePrint


object Main extends App {

  if(args.length == 4){
    runApp(args(0), args(1), args(2), args(3))
  }else{
    printUsage
  }

  def printUsage(): Unit ={
    println("trelloq <trello-api-token> <trello-api-key> <boardId> <sql-query>")
  }

  def runApp(token: String, key: String, boardId: String, query: String): Unit ={
    val restClient = new TrelloRestClient(token, key)
    val databaseGateway = new H2DatabaseGateway()
    val queryPrint = new ConsoleTablePrint()

    TrelloQ(restClient, databaseGateway, queryPrint).executeQuery(boardId, query)
  }


}