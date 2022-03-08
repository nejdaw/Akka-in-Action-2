import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object Counter {

  def apply(init: Int, max: Int): Behavior[Command] = {
    Behaviors.receive { (context, message) =>
      message match {
        case Increase =>
          val current = init + 1
          if (current <= max) {
            context.log.info(s"increasing to $current")
            apply(current, max)

          } else {
            context.log.info(s"I'm overloaded. Counting '$current' while max is '$max'")
            Behaviors.stopped
          }
      }
    }
  }

  sealed trait Command

  final case object Increase extends Command
}
