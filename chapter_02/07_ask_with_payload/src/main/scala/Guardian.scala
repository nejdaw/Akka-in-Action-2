import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object Guardian {

  def apply(): Behavior[Command] =
    Behaviors.setup { context =>
      val manager: ActorRef[Manager.Command] = context.spawn(Manager(), "manager-1")

      Behaviors.receiveMessage { case Start(tasks) =>
        manager ! Manager.Delegate(tasks)
        Behaviors.same
      }
    }

  sealed trait Command

  case class Start(tasks: List[String]) extends Command
}
