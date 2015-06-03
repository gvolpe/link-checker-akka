package com.gvolpe.linkchecker.actors

import akka.actor.{ActorRef, Actor}
import akka.pattern.pipe
import com.gvolpe.linkchecker.WebClient

object Cache {

  case class Get(url: String)
  case class Result(client: ActorRef, url: String, body: String)

}

class Cache extends Actor {

  import Cache._

  implicit val exec = context.dispatcher

  var cache = Map.empty[String, String]

  def receive = {
    case Get(url) =>
      if (cache contains url) sender ! cache(url)
      else {
        val client = sender()
        WebClient get url map (Result(client, url, _)) pipeTo self
      }
    case Result(client, url, body) =>
      cache += url -> body
      client ! body
  }

}
