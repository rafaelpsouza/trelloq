package br.eng.rafaelpsouza.trelloq.extract

import com.fasterxml.jackson.annotation.JsonProperty
import org.joda.time.DateTime

/**
  * Created by Rafael Souza on 23/12/17.
  */
case class Board(id: String, name: String, desc: String, url: String, shortUrl: String)

case class TrelloList(id: String, name: String, closed: Boolean, idBoard: String, pos: Double)

case class Card(id: String, closed: Boolean, desc: String, idBoard: String, idList: String, idMembersVoted: List[String],
                idLabels: List[String], name: String, pos: Double, shortLink: String, dueComplete: Boolean,
                idChecklists: List[String], idMembers: List[String], shortUrl: String, url: String, created: DateTime)

case class Action(id: String, idMemberCreator: String, data: ActionData, date: DateTime,
                  @JsonProperty("type") typee:String, member: Member, memberCreator: Member)

case class ActionData(board: Board, card: Card, idMember: String)

case class Member(id: String, avatarHash: String, bio: String, confirmed: Boolean, fullName: String,
                  initials: String, memberType: String, status: String, url: String, username: String,
                  avatarSource: String, email: String)

case class Label(id: String, idBoard: String, name: String, color: String, uses: Int)


