package com.gvolpe.linkchecker

import akka.actor.{Actor, ActorRef, Props}

class FosterParent(child: Props, probe: ActorRef) extends Actor {
  val childRef = context.actorOf(child, "child")
  def receive = {
    case msg if sender == context.parent =>
      probe forward msg
      childRef forward msg
    case msg =>
      probe forward msg
      context.parent forward msg
  }
}
