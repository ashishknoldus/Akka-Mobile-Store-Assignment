import akka.actor.{ActorSystem, Props}
import akka.testkit.{CallingThreadDispatcher, EventFilter, TestKit}
import com.knoldus.{PurchaseRequest, ValidationActor}
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}

/**
  * Created by knoldus on 22/3/17.
  */

object ValidationActorSpec {
  val testSystem = {
    val config = ConfigFactory.parseString(
      """
        |akka.loggers = [akka.testkit.TestEventListener]
      """.stripMargin
    )
    ActorSystem("test-system", config)
  }
}

import ValidationActorSpec._


class ValidationActorSpec   extends TestKit(testSystem) with WordSpecLike
  with BeforeAndAfterAll with MustMatchers {


  override protected def afterAll(): Unit = {
    system.terminate()
  }

  "com.knoldus.ValidationActor" must {

    "log forwarding message when mobiles are available" in {

        val dispatcherId = CallingThreadDispatcher.Id
        val props = Props(classOf[ValidationActor], testActor).withDispatcher(dispatcherId)

        val ref = system.actorOf(props)

        EventFilter.info(message = "Forwarding to purchase actor while available mobiles are : 3", occurrences = 1)
          .intercept {
            ref ! PurchaseRequest("Ashish", "Noida", 6781932L, 9876543210L, "S8")
          }

    }

    "The actor forwards to next actor" in {

      val dispatcherId = CallingThreadDispatcher.Id
      val props = Props(classOf[ValidationActor], testActor).withDispatcher(dispatcherId)

      val ref = system.actorOf(props)

      ref ! PurchaseRequest("Ashish", "Noida", 6781932L, 9876543210L, "S8")

      expectMsgPF() {
        case pr: PurchaseRequest =>
          pr must be(PurchaseRequest("Ashish", "Noida", 6781932L, 9876543210L, "S8"))
      }
    }

  }

}
