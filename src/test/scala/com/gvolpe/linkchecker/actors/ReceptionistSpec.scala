package com.gvolpe.linkchecker.actors

import akka.actor.{Actor, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._

object ReceptionistSpec {

  def fakeReceptionist: Props = Props(
    new Receptionist {
      override def controllerProps = Props[FakeController]
    }
  )

  class FakeController extends Actor {
    import context.dispatcher
    def receive = {
      case Controller.Check(url, depth) =>
        context.system.scheduler.scheduleOnce(1 second, sender, Controller.Result(Set(url)))
    }
  }

}

class ReceptionistSpec extends TestKit(ActorSystem("ReceptionistSpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  import Receptionist._
  import ReceptionistSpec._

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A receptionist" must {

    "reply with all result" in {
      val receptionist = system.actorOf(fakeReceptionist, "sendResult")
      receptionist ! Get("myUrl")
      expectMsg(Result("myUrl", Set("myUrl")))
    }

    "reject request flood" in {
      val receptionist = system.actorOf(fakeReceptionist, "requestFlood")
      for (i <- 1 to 5) receptionist ! Get(s"myUrl$i")
      expectMsg(Failed("myUrl5"))
      for (i <- 1 to 4) expectMsg(Result(s"myUrl$i", Set(s"myUrl$i")))
    }

  }

}
