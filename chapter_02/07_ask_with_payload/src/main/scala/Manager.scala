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
            val worker: ActorRef[Worker.Command] =
              context.spawn(Worker(), s"worker-$task")
            context.ask(worker, Worker.Do(Worker.Task(System.currentTimeMillis().toString, task), _)) {
              case Success(Worker.Done(taskId)) =>
                Report(s"$taskId has been finished by ${worker}")
              case Failure(ex) =>
                Report(s"task has failed with [${ex.getMessage}")
            }
          }
          Behaviors.same
        case Report(outline) =>
          context.log.info(outline)
          Behaviors.same
      }
    }

  sealed trait Command

  final case class Delegate(tasks: List[String]) extends Command

  final case class Report(outline: String) extends Command

  def multiply(x: Int)(y: Int) = x * y

  def multBy5(y: Int) = multiply(5)(y)
}
