import akka.actor.typed.ActorSystem

/**   1. Payload
  *   1. Currying
  */

object ManagerWorkerApp extends App {

  val system: ActorSystem[Guardian.Command] = ActorSystem(Guardian(), "example-ask-with-payload")

  system ! Guardian.Start(List("task-a", "task-b", "task-c", "task-d"))
}
