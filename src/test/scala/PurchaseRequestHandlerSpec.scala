import akka.actor.{ActorSystem, Props}
import akka.testkit.{CallingThreadDispatcher, EventFilter, TestKit}
import com.knoldus.{PurchaseRequest, PurchaseRequestHandler}
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}

/**
  * Created by knoldus on 22/3/17.
  */

object PurchaseRequestHandlerSpec {
  val testSystem = {
    val config = ConfigFactory.parseString(
      """
        |akka.loggers = [akka.testkit.TestEventListener]
      """.stripMargin
    )
    ActorSystem("test-system", config)
  }
}

import PurchaseRequestHandlerSpec._

class PurchaseRequestHandlerSpec extends TestKit(testSystem) with WordSpecLike
  with BeforeAndAfterAll with MustMatchers{

  override protected def afterAll(): Unit = {
    system.terminate()
  }

  "com.knoldus.PurchaseRequestHandler" must {

    "log Purchase request handler when receives a request" in {
      val dispatcherId = CallingThreadDispatcher.Id
      val props = Props(classOf[PurchaseRequestHandler], testActor).withDispatcher(dispatcherId)

      val ref = system.actorOf(props)

      EventFilter.info(message = "Purchase request handler", occurrences = 1)
        .intercept{
          ref ! PurchaseRequest("Ashish", "Noida", 6781932L, 9876543210L, "S8")
        }
    }

    "log error when handler receives bad request" in {

      val dispatcherId = CallingThreadDispatcher.Id
      val props = Props(classOf[PurchaseRequestHandler], testActor).withDispatcher(dispatcherId)

      val ref = system.actorOf(props)

      EventFilter.info(message = "Invalid request", occurrences = 1)
        .intercept{
          ref ! "XYZ"
        }

    }


    "The com.knoldus.PurchaseRequestHandler forwards to next actor" in {

      val dispatcherId = CallingThreadDispatcher.Id
      val props = Props(classOf[PurchaseRequestHandler], testActor).withDispatcher(dispatcherId)

      val ref = system.actorOf(props)

      ref ! PurchaseRequest("Ashish", "Noida", 6781932L, 9876543210L, "S8")

      expectMsgPF() {
        case pr: PurchaseRequest =>
          pr must be(PurchaseRequest("Ashish", "Noida", 6781932L, 9876543210L, "S8"))
      }
    }


  }

}
