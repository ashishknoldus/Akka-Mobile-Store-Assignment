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

}
