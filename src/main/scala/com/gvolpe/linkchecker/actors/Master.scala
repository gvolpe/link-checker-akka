package com.gvolpe.linkchecker.actors

import akka.actor.{Props, ReceiveTimeout, Actor}
import com.gvolpe.linkchecker.WebClient
import scala.concurrent.duration._

object Master {

  case object Start

  def props = Props[Master]

}

class Master extends Actor {

  import Master._

  context.setReceiveTimeout(10 seconds)

  def receive = idle

  def idle: Receive ={
    case Start =>
      val receptionist = context.actorOf(Receptionist.props, "receptionist")

      receptionist ! Receptionist.Get("http://www.google.com")
      receptionist ! Receptionist.Get("http://www.google.com/1")
      receptionist ! Receptionist.Get("http://www.google.com/2")
      receptionist ! Receptionist.Get("http://www.google.com/3")

      context.become(running)
  }

  def running: Receive = {
    case Receptionist.Result(url, links) =>
      println(links.toVector.sorted.mkString(s"Results for '$url':\n", "\n", "\n"))
    case Receptionist.Failed(url) =>
      println(s"Failed to fetch '$url'\n")
    case ReceiveTimeout =>
      context.stop(self)
  }

  override def postStop(): Unit = {
    context.system.shutdown()
  }

}
