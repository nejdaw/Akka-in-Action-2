import akka.actor.typed.ActorSystem

object TimeApp extends App {

  val guardian = ActorSystem[Timing.Command](Timing(), "timing-example")

  guardian ! Timing.Start
}
