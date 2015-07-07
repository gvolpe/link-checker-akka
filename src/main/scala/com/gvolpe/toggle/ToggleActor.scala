package com.gvolpe.toggle

import akka.actor.{Actor, Props}

object ToggleActor {

  def props = Props[ToggleActor]

}

class ToggleActor extends Actor {

  def receive = happy

  def happy: Receive = {
    case "how are you?" =>
      sender ! "happy"
      context become sad
  }

  def sad: Receive = {
    case "how are you?" =>
      sender ! "sad"
      context become happy
  }

}
