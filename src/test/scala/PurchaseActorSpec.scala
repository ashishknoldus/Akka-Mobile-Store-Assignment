import akka.actor.{ActorSystem, Props}
import akka.routing.FromConfig
import akka.testkit.{CallingThreadDispatcher, EventFilter, TestKit}
import com.knoldus.{PurchaseActor, PurchaseRequest}
import com.typesafe.config.{Config, ConfigFactory}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}

/**
  * Created by knoldus on 22/3/17.
  */


object PurchaseActorSpec {
  val testSystem = {
    val config = ConfigFactory.parseString(
      """
        |akka.loggers = [akka.testkit.TestEventListener]
      """.stripMargin
    )
    ActorSystem("test-system", config)
  }
}

import PurchaseActorSpec._

class PurchaseActorSpec  extends TestKit(testSystem) with WordSpecLike
  with BeforeAndAfterAll with MustMatchers {


  override protected def afterAll(): Unit = {
    system.terminate()
  }

  "com.knoldus.PurchaseActor" must {

    "log Purchase request handler when receives a request" in {

      println("First test case")

      val config: Config = ConfigFactory.parseString(
        """
          |akka.actor.deployment {
          | /purchaseActorPool {
          |   router = balancing-pool
          |   resizer {
          |      pressure-threshold = 0
          |      lower-bound = 2
          |      upper-bound = 5
          |      messages-per-resize = 1
          |    }
          | }
          |}
        """.stripMargin
      )

      val dispatcherId = CallingThreadDispatcher.Id

      val ref =  system.actorOf(Props[PurchaseActor].withDispatcher(dispatcherId))

      val request = PurchaseRequest("Ashish", "Noida", 6781932L, 9876543210L, "S8")

      EventFilter.info(message = s"Your mobile phone has been booked with request $request", occurrences = 1)
        .intercept{
          ref ! request
        }
    }

  }

}
