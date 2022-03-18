import akka.actor.testkit.typed.scaladsl.ActorTestKit
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

class AsyncTestingExampleSpec extends AnyWordSpec with BeforeAndAfterAll with Matchers {

  val testKit = ActorTestKit()

  "Actor Echo" must {

    "return same string" in {

      val echo    = testKit.spawn(Echo(), "Akka")
      val probe   = testKit.createTestProbe[String]()
      val content = "Heellooo"
      echo ! Echo.Sound(content, probe.ref)
      probe.expectMessage(content)

    }
  }

  "A Counter" must {

    "Increase its value" in {
      val counter = testKit.spawn(Counter(count = 0), "counter")
      val probe   = testKit.createTestProbe[Counter.Event]()
      counter ! Counter.Increase
      counter ! Counter.GetState(probe.ref)
      probe.expectMessage(Counter.State(1))
    }
  }

  // We need to shut down test kit!
  override def afterAll(): Unit = testKit.shutdownTestKit()

}

object Echo {

  def apply(): Behavior[Sound] =
    Behaviors.receiveMessage { msg =>
      msg.from ! msg.content
      Behaviors.same
    }

  case class Sound(content: String, from: ActorRef[String])
}

object Counter {

  sealed trait Command
  case class GetState(replyTo: ActorRef[Event]) extends Command
  case object Increase                          extends Command

  sealed trait Event
  case class State(count: Int) extends Event

  def apply(count: Int): Behavior[Command] =
    Behaviors.receiveMessage {
      case GetState(replyTo) =>
        replyTo ! State(count)
        Behaviors.same
      case Increase =>
        apply(count + 1)
    }
}
