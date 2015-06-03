name := """link-checker-akka"""

version := "1.0"

scalaVersion := "2.11.6"

// Change this to another test framework if you prefer
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.11",
  "com.ning" % "async-http-client" % "1.7.19",
  "org.jsoup" % "jsoup" % "1.8.1",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)
