package com.gvolpe.linkchecker.actors

import akka.actor.{Props, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import com.gvolpe.linkchecker.{StepParent, FakeWebClient}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class GetterSpec extends TestKit(ActorSystem("TestSys")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  import FakeWebClient._

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  def fakeGetter(url: String, depth: Int): Props =
    Props(new Getter(url, depth) {
      override def client = FakeWebClient
    })

  "A getter" must {

    "return the right body" in {
      system.actorOf(Props(new StepParent(fakeGetter(firstLink, 2), testActor)), "rightBody")
      for (link <- links(firstLink))
        expectMsg(Controller.Check(link, 2))
      expectMsg(Getter.Done)
    }

    "properly finish in case of errors" in {
      system.actorOf(Props(new StepParent(fakeGetter("unknown", 2), testActor)), "wrongBody")
      expectMsg(Getter.Done)
    }

  }

}
