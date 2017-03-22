import akka.actor.{Actor, ActorLogging, ActorRef, Props}

/**
  * Created by knoldus on 21/3/17.
  */

case object MyCaseClass

class ValidationActor(purchaseActor: ActorRef) extends Actor with ActorLogging {

  private var availableMobiles = 3

  override def receive: Receive = {

    case pr: PurchaseRequest =>

      if (availableMobiles <= 0) {
        log.error("The item is out of stock")
      } else {
        log.info(s"Forwarding to purchase actor while available mobiles are : $availableMobiles")
        availableMobiles -= 1
        purchaseActor.forward(pr)
      }
  }
}
