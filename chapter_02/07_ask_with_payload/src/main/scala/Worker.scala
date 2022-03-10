import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

object Worker {

  def apply(): Behavior[Command] =
    Behaviors.receive { (context, message) =>
      message match {
        case Do(task, replyTo) =>
          doing(scala.util.Random.between(2000, 4000))
          context.log.info {
            s"My name is '${context.self.path.name}'. " +
              s"And I've done task with id ${task.taskId}: ${task.taskDescription}"
          }
          replyTo ! Worker.Done(task.taskId)
          Behaviors.stopped
      }
    }

  def doing(duration: Int): Unit = {
    val endTime = System.currentTimeMillis + duration
    while (endTime > System.currentTimeMillis) {}
  }

  sealed trait Response

  sealed trait Command

  final case class Task(taskId: String, taskDescription: String)

  final case class Do(task: Task, replyTo: ActorRef[Worker.Response]) extends Command

  case class Done(taskId: String) extends Response
}
