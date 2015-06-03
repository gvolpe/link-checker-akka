package com.gvolpe.linkchecker

import akka.actor.ActorSystem
import com.gvolpe.linkchecker.actors.{Master, Receptionist}

object LinkCheckerApp extends App {

  val system = ActorSystem("linkCheckerSystem")

  val master = system.actorOf(Master.props, "master")

  master ! Master.Start

}
