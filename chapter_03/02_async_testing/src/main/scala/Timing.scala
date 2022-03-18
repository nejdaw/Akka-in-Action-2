import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import scala.concurrent.duration._

object Timing {

  def apply(): Behavior[Command] =
    Behaviors.receive { (context, message) =>
      // Factory method to enable timers
      Behaviors.withTimers[Command] { timers =>
        message match {
          case Start =>
            context.log.info("started")
            // Send a single message to self after delay.
            timers.startSingleTimer(Timeout, 5.second)

            // Sends messages repeatedly with fixed delay to self
            timers.startTimerWithFixedDelay(CheckingKey, Checking, 1.second)
          case Timeout =>
            context.log.info("time is out")
            timers.cancel(CheckingKey)
          case Checking =>
            context.log.info("just checking")
        }
        Behaviors.same
      }

    }

  // Protocol
  sealed trait Command

  case object Start extends Command

  private object Timeout extends Command

  private object Checking extends Command

  private object CheckingKey

}
