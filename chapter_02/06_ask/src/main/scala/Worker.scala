import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

object Worker {

  def apply(): Behavior[Command] =
    Behaviors.receive { (context, message) =>
      message match {
        case Do(replyTo) =>
          doing(scala.util.Random.between(2000, 4000))
          context.log.info(s"My name is '${context.self.path.name}'. And I've done my task")
          replyTo ! Worker.Done
          Behaviors.stopped
      }
    }

  def doing(duration: Int): Unit = {
    val endTime = System.currentTimeMillis + duration
    while (endTime > System.currentTimeMillis) {}
  }

  sealed trait Command

  sealed trait Response

  case class Do(replyTo: ActorRef[Worker.Response]) extends Command

  case object Done extends Response
}
