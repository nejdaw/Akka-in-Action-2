import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object Guardian {

  def apply(): Behavior[Command] =
    Behaviors.setup { context =>
      val manager: ActorRef[Manager.Command] = context.spawn(Manager(1), "manager-1")

      Behaviors.receiveMessage { case Start(tasks) =>
        tasks.map { task =>
          manager ! Manager.Delegate(task)
        }
        Behaviors.same
      }
    }

  sealed trait Command

  case class Start(tasks: List[String]) extends Command
}
