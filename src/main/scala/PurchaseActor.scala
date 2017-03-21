import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.routing.FromConfig
import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by knoldus on 21/3/17.
  */
class PurchaseActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case pr: PurchaseRequest => {
      log.info(s"Your mobile phone has been booked with request $pr")
    }
  }
}

object PurchaseActor {

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

  val system: ActorSystem = ActorSystem("RouterSystem", config)

  val router: ActorRef = system.actorOf(FromConfig.props(Props[PurchaseActor]), "purchaseActorPool")

}
