import com.typesafe.sbt.SbtNativePackager._

name := "trelloq"

version := "1.0"

organization := "br.eng.rafaelsouza"

scalaVersion := "2.10.5"

packageArchetype.java_application

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.10" % "2.1.7" % "test",
  "junit" % "junit" % "4.11" % "test",
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.10" % "2.2.2",
  "com.fasterxml.jackson.datatype" % "jackson-datatype-joda" % "2.2.2",
  "org.joda" % "joda-convert" % "1.6",
  "org.scalaj" %% "scalaj-http" % "1.0.1",
  "org.scalikejdbc" %% "scalikejdbc"       % "3.1.0",
  "com.h2database"  %  "h2"                % "1.4.196",
  "ch.qos.logback"  %  "logback-classic"   % "1.2.3",
  "de.vandermeer" % "asciitable" % "0.3.2"
)

