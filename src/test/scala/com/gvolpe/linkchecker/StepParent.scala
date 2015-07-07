package com.gvolpe.linkchecker

import akka.actor.{Actor, ActorRef, Props}

class StepParent(child: Props, probe: ActorRef) extends Actor {
  context.actorOf(child, "child")
  def receive = {
    case msg => probe tell (msg, sender())
  }
}
