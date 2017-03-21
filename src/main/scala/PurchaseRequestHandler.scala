import akka.actor.{Actor, ActorLogging, ActorRef, Props}

/**
  * Created by knoldus on 21/3/17.
  */
class PurchaseRequestHandler extends Actor with ActorLogging {

  override def receive: Receive = {
    case pr: PurchaseRequest =>
      log.info("Purchase request handler")
      ValidationActor.router.forward(pr)
    case _ => log.info("Invalid request")
  }

}

object PurchaseRequestHandler {

  val props: Props = Props[PurchaseRequestHandler]
  val router: ActorRef = Main.system.actorOf(props)

}
