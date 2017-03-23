package com.knoldus

import akka.actor.{Actor, ActorLogging, ActorRef}

/**
  * Created by knoldus on 21/3/17.
  */
class PurchaseRequestHandler(validationActor: ActorRef) extends Actor with ActorLogging {

  override def receive(): Receive = {
    case pr: PurchaseRequest =>
      log.info("Purchase request handler")
      validationActor.forward(pr)
    case _ => log.info("Invalid request")
  }

}
