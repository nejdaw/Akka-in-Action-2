import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

object Worker {

  def apply(): Behavior[Command] =
    idle(0)

  def cleaning(jobsDone: Int): Behavior[Command] =
    Behaviors.receive[Command] { (context, message) =>
      message match {
        case Do(manager, task) =>
          context.log.info(s"${context.self.path.name}: can't do '$task' now. I need to clean first")
          manager ! Cleaning(task)
          Behaviors.same
        case Clean =>
          doing(1000)
          context.log.info(s"${context.self.path.name}: I'm DONE cleaning")
          idle(jobsDone)
      }
    }

  def idle(jobsDone: Int): Behavior[Command] =
    Behaviors.receive[Command] { (context, message) =>
      message match {
        case Do(manager, task) =>
          doing(999)
          val jobSoFar = jobsDone + 1
          context.log.info(s"${context.self.path.name}': I've done '$jobSoFar' job(s) so far")
          manager ! JobDone(task)
          context.self ! Clean
          cleaning(jobSoFar)
        case Clean =>
          context.log.info(s"${context.self.path.name} : nothing to clean yet")
          Behaviors.same
      }
    }

  def doing(duration: Int): Unit = {
    val endTime = System.currentTimeMillis + duration
    while (endTime > System.currentTimeMillis) {}
  }

  sealed trait Command

  sealed trait Response

  final case class Do(replyTo: ActorRef[Worker.Response], task: String) extends Command

  final case class Cleaning(task: String) extends Response

  final case class JobDone(task: String) extends Response

  final case object Clean extends Command
}
