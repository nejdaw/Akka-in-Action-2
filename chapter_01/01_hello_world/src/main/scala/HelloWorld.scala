import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object HelloWorld {
  def apply(): Behavior[String] = {
    Behaviors.receive { (context, message) =>
      context.log.info(s"received message '$message'")
      Behaviors.same
    }
  }
}
