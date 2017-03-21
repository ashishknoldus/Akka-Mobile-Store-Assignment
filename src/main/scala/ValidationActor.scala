import akka.actor.{Actor, ActorLogging, ActorRef, Props}

/**
  * Created by knoldus on 21/3/17.
  */
class ValidationActor extends Actor with ActorLogging {

  var availableMobiles = 3

  override def receive: Receive = {

    case pr: PurchaseRequest =>

      if (availableMobiles <= 0) {
        log.error("The item is out of stock")
      } else {
        log.info(s"Forwarding to purchase actor while avalable mobiles are : $availableMobiles")
        availableMobiles -= 1
        PurchaseActor.router.forward(pr)
      }

  }
}

object ValidationActor {

  val props: Props = Props[ValidationActor]
  val router: ActorRef = Main.system.actorOf(props)

}
