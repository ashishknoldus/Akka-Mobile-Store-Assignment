import akka.actor.{ActorRef, ActorSystem, Props}
import akka.routing.FromConfig
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.duration.DurationInt

/**
  * Created by knoldus on 21/3/17.
  */
class Main {

}

object Main {

  def main(args: Array[String]): Unit = {

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

    val system = ActorSystem("RouterSystem", config)

    val purchaseActorRouter: ActorRef = system.actorOf(FromConfig.props(Props[PurchaseActor]), "purchaseActorPool")

    val validationActor: ActorRef = system.actorOf(Props(classOf[ValidationActor], purchaseActorRouter), "validationActor")

    val purchaseRequestHandler: ActorRef = system.actorOf(Props(classOf[PurchaseRequestHandler], validationActor), "purchaseRequestHandler")


    implicit val timeout = Timeout(1000 seconds)

    purchaseRequestHandler ! PurchaseRequest("Ashish", "Noida", 6781932L, 9876543210L, "S8")
    purchaseRequestHandler ! PurchaseRequest("Neha", "Noida", 93345345L, 984675527L, "S8")
    purchaseRequestHandler ! PurchaseRequest("Charmy", "Noida", 6781932L, 8974560320L, "S8")
    purchaseRequestHandler ! PurchaseRequest("Vijay", "Noida", 6781932L, 9993332292L, "S8")
    purchaseRequestHandler ! PurchaseRequest("Krishna", "Noida", 44554533L, 9876543210L, "S8")
    purchaseRequestHandler ! PurchaseRequest("Ishwar", "Noida", 6781932L, 882737723L, "S8")

  }
}
