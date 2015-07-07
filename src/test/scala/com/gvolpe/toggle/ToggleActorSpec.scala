package com.gvolpe.toggle

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{Matchers, BeforeAndAfterAll, WordSpecLike}

import scala.concurrent.duration._

class ToggleActorSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("ToggleActorSpec"))

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "Toggle actor" must {

    "reply with its current state" in {
      val toggle = system.actorOf(ToggleActor.props)
      toggle ! "how are you?"
      expectMsg("happy")
      toggle ! "how are you?"
      expectMsg("sad")
      toggle ! "unknown"
      expectNoMsg(1 second)
      system.shutdown()
    }

  }

}
