package br.eng.rafaelpsouza.trelloq.print

import br.eng.rafaelpsouza.trelloq.{QueryPrint, QueryResult}
import de.vandermeer.asciitable.{AsciiTable, CWC_LongestLine, CWC_LongestWordMax}
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment

import scala.collection.JavaConverters._

/**
  * Created by Rafael Souza on 27/12/17.
  */
class ConsoleTablePrint extends QueryPrint {

  override def print(result: QueryResult): Unit = {
    if(result.values.isEmpty){
      println("No results")
    }else{
      printTable(result)
    }
  }

  private def printTable(result: QueryResult): Unit ={
    val asciiTable = new AsciiTable()
    asciiTable.addRule()
    asciiTable.addRow(result.header.asJavaCollection).setTextAlignment(TextAlignment.CENTER)
    asciiTable.addRule()
    result.values.foreach(row => {
      asciiTable.addRow(result.header.map(header => cellToScring(row(header))).asJavaCollection).setTextAlignment(TextAlignment.CENTER)
      asciiTable.addRule()
    })
    println(asciiTable.render())
  }

  private def cellToScring(value: Any): String ={
    if(value.toString.length() == 0) return " "

    return value.toString
  }
}
