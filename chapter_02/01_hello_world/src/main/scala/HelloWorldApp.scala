import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}

/**   1. Actor system is an actor
  *   1. Sending message to an actor
  *   1. Logging in an actor
  *   1. Context
  *   1. ActorRef
  *   1. ActorSystem is an actor and top level guardian
  *   1. Another possible message - signal
  *   1. Next behavior
  */

object HelloWorldApp extends App {
  val guardian: ActorRef[String] = ActorSystem(HelloWorld.apply(), "HelloWorld")

  guardian.tell("Hello")
  guardian ! "Hello Again"
}
