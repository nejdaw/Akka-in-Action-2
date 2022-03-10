import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import akka.util.Timeout

import scala.concurrent.duration.SECONDS
import scala.util.{Failure, Success}

object Manager {

  def apply(): Behavior[Command] =
    Behaviors.setup { context =>
      implicit val timeout: Timeout = Timeout(3, SECONDS)

      Behaviors.receiveMessage {
        case Delegate(tasks) =>
          tasks.foreach { task =>
            val worker: ActorRef[Worker.Command] = context.spawn(Worker(), s"worker-$task")

            context.ask(worker, Worker.Do) {
              case Success(Worker.Done) =>
                Report(s"$task has been finished by ${worker}")
              case Failure(ex) =>
                Report(s"task '$task' has failed with [${ex.getMessage}")
            }
          }
          Behaviors.same
        case Report(description) =>
          context.log.info(description)
          Behaviors.same
      }
    }

  sealed trait Command

  final case class Delegate(tasks: List[String]) extends Command

  final case class Report(description: String) extends Command
}
