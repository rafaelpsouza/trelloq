package br.eng.rafaelpsouza.trelloq.extract

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.joda.time.DateTime

import scalaj.http.{Http, HttpResponse}

/**
  * Created by Rafael Souza on 23/12/17.
  */
class TrelloRestClient(token: String, key: String) {

  def boardById(boardId: String): Board = {
    val requestURL = s"https://trello.com/1/boards/${boardId}/"
    val response: HttpResponse[String] = Http(requestURL).params(
      ("key", key), ("token", token)).asString

    return JsonJacksonSerialization.fromJson[Board](response.body)
  }

  def listsFromBoard(boardId: String): Seq[TrelloList] = {
    val requestURL = s"https://trello.com/1/boards/${boardId}/lists"
    val response: HttpResponse[String] = Http(requestURL).params(
      ("key", key), ("token", token)).asString

    return JsonJacksonSerialization.fromJson[List[TrelloList]](response.body)
  }

  def labelsFromBoard(boardId: String): Seq[Label] = {
    val requestURL = s"https://trello.com/1/boards/${boardId}/labels"
    val response: HttpResponse[String] = Http(requestURL).params(
      ("key", key), ("token", token)).asString

    return JsonJacksonSerialization.fromJson[List[Label]](response.body)
  }

  def cardsFromList(listId: String): Seq[Card] = {
    val requestURL = s"https://trello.com/1/lists/${listId}/cards"
    val response: HttpResponse[String] = Http(requestURL).params(
      ("key", key), ("token", token)).asString

    return JsonJacksonSerialization.fromJson[List[Card]](response.body).map(card => card.copy(created = getCreatedDate(card.id)))
  }

  def actionsFromCard(cardId: String): Seq[Action] = {
    val requestURL = s"https://trello.com/1/cards/${cardId}/actions"
    val response: HttpResponse[String] = Http(requestURL).params(
      ("key", key), ("token", token)).asString

    return JsonJacksonSerialization.fromJson[List[Action]](response.body)
  }


  private def getCreatedDate(cardId: String):DateTime = {
    val unixTimestamp = Integer.parseInt(cardId.substring(0, 8), 16)
    new DateTime(unixTimestamp * 1000L)
  }

}

private object JsonJacksonSerialization {

  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.registerModule(new JodaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  def toJson(value: Map[Symbol, Any]): String = {
    toJson(value map { case (k,v) => k.name -> v})
  }

  def toJson(value: Any): String = {
    mapper.writeValueAsString(value)
  }

  def toMap[V](json:String)(implicit m: Manifest[V]) = fromJson[Map[String,V]](json)

  def fromJson[T](json: String)(implicit m : Manifest[T]): T = {
    mapper.readValue[T](json)
  }
}