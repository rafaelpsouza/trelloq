package br.eng.rafaelpsouza.trelloq.database

import java.sql.{ResultSetMetaData, Timestamp}

import br.eng.rafaelpsouza.trelloq._
import br.eng.rafaelpsouza.trelloq.extract.{Board, Card, Label, TrelloList}
import org.h2.jdbc.JdbcClob
import scalikejdbc._


/**
  * Created by Rafael Souza on 21/12/17.
  */


class H2DatabaseGateway extends DatabaseGateway {

  override def createSchema(): Unit = H2DatabaseGateway.createSchema()

  override def saveBoard(board: Board) = H2DatabaseGateway.saveBoard(board)

  override def saveList(list: TrelloList) = H2DatabaseGateway.saveList(list)

  override def saveLabel(label: Label) = H2DatabaseGateway.saveLabel(label)

  override def saveCard(card: Card) = H2DatabaseGateway.saveCard(card)

  override def query(query: String) = H2DatabaseGateway.query(query)
}

private object H2DatabaseGateway {

  GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(
    enabled = false,
    singleLineMode = false,
    printUnprocessedStackTrace = false,
    stackTraceDepth= 15,
    logLevel = 'error,
    warningEnabled = false,
    warningThresholdMillis = 3000L,
    warningLogLevel = 'warn
  )

  val boadTable = sql"""create table board (id varchar(64) not null primary key, name clob, desc clob, url clob, shortUrl clob)"""
  val labelTable = sql""" create table label (id varchar(64) not null primary key, idBoard varchar(64), name clob, color varchar(20), uses bigint) """
  val listTable = sql""" create table list (id varchar(64) not null primary key, idBoard varchar(64), name clob, closed boolean, pos decimal) """
  val cardTable =
    sql""" create table card (id varchar(64) not null primary key, closed boolean, desc clob, idBoard varchar(64),
         idList varchar(64),  name clob, pos decimal, shortlink clob, duecomplete boolean, shorturl clob, url clob, created timestamp) """
  val cardLabelTable = sql""" create table card_label (idCard varchar(64), idLabel varchar(64)) """


  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:h2:mem:trellometrics", "", "")

  def createSchema()(implicit session: DBSession = AutoSession): Unit = {
    boadTable.execute.apply()
    labelTable.execute.apply()
    listTable.execute.apply()
    cardTable.execute.apply()
    cardLabelTable.execute.apply()
  }

  def saveBoard(board: Board)(implicit session: DBSession = AutoSession): String = {
    sql""" insert into board values (${board.id}, ${board.name}, ${board.desc}, ${board.url}, ${board.shortUrl}) """.execute().apply()

    return board.id
  }

  def saveList(list: TrelloList)(implicit session: DBSession = AutoSession): String = {
    sql""" insert into list values  (${list.id}, ${list.idBoard}, ${list.name}, ${list.closed}, ${list.pos}) """.execute().apply()

    return list.id
  }

  def saveLabel(label: Label)(implicit session: DBSession = AutoSession): String = {
    sql""" insert into label values (${label.id}, ${label.idBoard}, ${label.name}, ${label.color}, ${label.uses}) """.execute().apply()

    return label.id
  }

  def saveCard(card: Card)(implicit session: DBSession = AutoSession): String = {
    sql""" insert into card values  (${card.id}, ${card.closed}, ${card.desc}, ${card.idBoard}, ${card.idList},
          ${card.name}, ${card.pos}, ${card.shortLink}, ${card.dueComplete}, ${card.shortUrl}, ${card.url}, ${new Timestamp(card.created.getMillis)}) """.execute().apply()
    card.idLabels.foreach(idLabel => saveCardLabel(card.id, idLabel))

    return card.id
  }

  private def saveCardLabel(cardId: String, labelId: String)(implicit session: DBSession = AutoSession): Unit = {
    sql""" insert into card_label values  (${cardId}, ${labelId}) """.execute().apply()
  }

  def query(query: String)(implicit session: DBSession = AutoSession): QueryResult = {
    //new DateTime(momentFromDB, DateTimeZone.forID("anytimezone"))
    val results: List[(List[String], Map[String, Any])] = SQL(query).map(f => (getColumns(f.metaData), f.toMap())).list().apply()
    val header: List[String] = results.headOption.getOrElse((List[ String](), Map()))._1

    return QueryResult(header, results.map(row => row._1.map(key => convertValues(key -> row._2.get(key).getOrElse(" "))).toMap))
  }


  private def getColumns(metaData: ResultSetMetaData): List[String] = {
    return (1 to metaData.getColumnCount).map(index => metaData.getColumnName(index)).toList
  }

  private def convertValues(entry: (String, Any)): (String, Any) = {
    if (entry._2.isInstanceOf[JdbcClob]) {
      val clobAsString:String = entry._2.asInstanceOf[JdbcClob].getSubString(1, entry._2.asInstanceOf[JdbcClob].length().asInstanceOf[Int])

      return (entry._1, clobAsString)
    }

    return entry
  }
}
