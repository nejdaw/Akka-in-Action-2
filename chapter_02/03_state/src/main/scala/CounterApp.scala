import akka.actor.typed.ActorSystem

/**   1. How can we represent state (val in Counter)
  *   1. We return behavior
  */

object CounterApp extends App {

  val guardian: ActorSystem[Counter.Command] = ActorSystem(Counter(init = 0, max = 2), "counter")

  guardian ! Counter.Increase
  guardian ! Counter.Increase
  guardian ! Counter.Increase

}
