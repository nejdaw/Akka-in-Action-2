import akka.actor.typed.ActorSystem

/**   1. Timeout
  *   1. Ask - creates interim actor
  *   1. Dead letter queue
  *   1. If worker is busy cleaning the manager will send next task to someone else
  *   1. How `replyTo` reference is being created
  */

object ManagerWorkerApp extends App {

  val system: ActorSystem[Guardian.Command] = ActorSystem(Guardian(), "example-ask-without-content")

  system ! Guardian.Start(List("task-a", "task-b", "task-c", "task-d"))
}
