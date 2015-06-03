package com.gvolpe.linkchecker.actors

import akka.actor.{Props, Actor}
import akka.pattern.pipe
import com.gvolpe.linkchecker.{LinksFinder, WebClient}

object Getter {

  case object Done
  case object Abort

  def props(url: String, depth: Int) = Props(new Getter(url, depth))

}

class Getter(url: String, depth: Int) extends Actor {

  import Getter._

  implicit val exec = context.dispatcher

  WebClient get(url) pipeTo self

  def receive = {
    case body: String =>
      for (link <- LinksFinder.find(body))
        context.parent ! Controller.Check(link, depth)
      stop()
    case Abort => stop()
    case _ => stop() //see Status.Failure

  }

  def stop() = {
    context.parent ! Done
    context.stop(self)
  }

}
