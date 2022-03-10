import akka.actor.typed.ActorSystem

/**   1. Show how door FMS would work
  *   1. An actor can send message to itself
  *   1. Worker has two states - idle and cleaning
  *   1. If worker is busy cleaning the manager will send next task to someone else
  */

object FSMApp extends App {

  val system: ActorSystem[Guardian.Command] = ActorSystem(Guardian(), "finite-state-machine")

  system ! Guardian.Start(List("one", "two", "three"))

}
