import actors.Guardian
import akka.actor.typed.ActorSystem

/**   1. How to create new actors
  *      1. Sending message to an actor
  *      1. Show diagram to make it easier to undersatnd
  *      1. Actor created from context will have different guardian
  *      1. Manager will be supervisor for a Worker
  *      1. Adapter - a pattern, and its main use case applies when one request will be followed by many responses.
  *      1. Adapter as an actor that is created to modify the message
  *      1. Behavior.setup - This is a behavior that runs only once. It’s usually used to initiate the state of the
  *         actor in the functional style
  */

object ErrorKernelApp extends App {

  val system: ActorSystem[Guardian.Command] = ActorSystem(Guardian(), "error-kernel")

  system ! Guardian.Start(List("one", "two"))
}
