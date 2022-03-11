package actors
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object Manager {

  def apply(): Behavior[Command] =
    Behaviors.setup { context =>
      val adapter: ActorRef[Worker.Response] = context.messageAdapter(rsp => WorkerDoneAdapter(rsp))

      Behaviors.receiveMessage {
        case Delegate(tasks) =>
          tasks.foreach { task =>
            val worker: ActorRef[Worker.Command] = context.spawn(Worker(), s"worker-$task")
            context.log.info(s"sending task '$task' to $worker")
            worker ! Worker.Do(adapter, task)
          }
          Behaviors.same
        case WorkerDoneAdapter(Worker.Done(task)) =>
          context.log.info(s"task '$task' has been finished")
          Behaviors.same
      }
    }

  sealed trait Command

  final case class Delegate(tasks: List[String]) extends Command

  final case class Report(task: String)

  private case class WorkerDoneAdapter(response: Worker.Response) extends Command
}
