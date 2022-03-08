package actors

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object Worker {

  def apply(): Behavior[Command] =
    Behaviors.receive { (context, message) =>
      message match {
        case Do(replyTo, task) =>
          context.log.info(
            s"'${context.self.path}'. Done with '$task'")
          replyTo ! Worker.Done(task)
          Behaviors.stopped
      }
    }

  sealed trait Command

  sealed trait Response

  final case class Do(replyTo: ActorRef[Worker.Response], task: String)
    extends Command

  final case class Done(task: String) extends Response
}
