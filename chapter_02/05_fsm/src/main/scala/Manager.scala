import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object Manager {

  def apply(manPower: Int): Behavior[Command] =
    Behaviors.setup { context =>
      val workers: Seq[ActorRef[Worker.Command]] =
        (0 to manPower).map(num => context.spawn(Worker(), s"worker-$num"))
      val adapter: ActorRef[Worker.Response] =
        context.messageAdapter(rsp => AdapterWorkerResponse(rsp))

      Behaviors.receiveMessage { message =>
        message match {
          case Delegate(task) =>
            val worker = workers(scala.util.Random.nextInt(workers.size))
            worker ! Worker.Do(adapter, task)
          case Done(task) =>
            context.log.info(s"task '$task' has been finished")
          case AdapterWorkerResponse(response) => {
            response match {
              case Worker.Cleaning(task) =>
                context.self ! Delegate(task)
              case Worker.JobDone(task) =>
                context.self ! Done(task)
            }
          }
        }
        Behaviors.same
      }
    }

  sealed trait Command

  final case class Delegate(task: String) extends Command

  final case class Done(task: String) extends Command

  final case class AdapterWorkerResponse(message: Worker.Response) extends Command
}
