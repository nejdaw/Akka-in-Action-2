import akka.actor.testkit.typed.scaladsl.BehaviorTestKit
import akka.actor.testkit.typed.scaladsl.TestInbox
import akka.actor.testkit.typed.CapturedLogEvent
import akka.actor.testkit.typed.Effect.{NoEffects, Scheduled, Spawned}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import org.scalatest.exceptions.TestFailedException
import org.slf4j.event.Level

import scala.concurrent.duration.DurationInt

class SyncTestingExampleSpec extends AnyWordSpec with Matchers {

  import SyncTestingExampleSpec._

  "Typed actor synchronous testing" must {

    "record spawning" in {
      // We use BehaviorTestKit
      val testKit = BehaviorTestKit(Hello.apply())
      testKit.expectEffect(NoEffects)
      testKit.run(Hello.Create("child"))
      testKit.expectEffect(Spawned(dullActor, "child"))
      testKit.expectEffect(NoEffects)
    }

    "how do I find dull Actor received a message" in {
      val testKit = BehaviorTestKit(Hello())
      val probe   = TestInbox[String]()
      testKit.run(Hello.Proxy("hello", probe.ref))
      probe.expectMessage("hello")
      probe.expectMessage("hello")
      probe.hasMessages shouldBe false
    }

    "record the log" in {
      val testKit = BehaviorTestKit(Hello())
      testKit.run(Hello.Done)
      testKit.logEntries() shouldBe Seq(CapturedLogEvent(Level.INFO, "it's done"))
    }

    // We can't test scheduling in using BehaviorTestKit!!
    "failing to schedule a message" in {
      intercept[TestFailedException] {
        val testKit = BehaviorTestKit(Hello())
        testKit.run(Hello.Schedule("name"))
        testKit.expectEffect(Scheduled(0.seconds, testKit.ref, Hello.Done))
        testKit.logEntries() shouldBe Seq(CapturedLogEvent(Level.INFO, "it's done"))
      }
    }
  }
}

object SyncTestingExampleSpec {

  val dullActor: Behaviors.Receive[String] = Behaviors.receiveMessage[String] { _ =>
    Behaviors.same[String]
  }

  object Hello {

    def apply(): Behavior[Command] =
      Behaviors.receive { (context, message) =>
        message match {
          case Create(name) =>
            context.spawn(dullActor, name)
            Behaviors.same
          case Proxy(message, sendTo) =>
            sendTo ! message
            sendTo ! message
            Behaviors.same
          case Schedule(name) =>
            context.scheduleOnce(0.seconds, context.self, Done)
            Behaviors.same
          case Done =>
            context.log.info(s"it's done")
            Behaviors.same
        }
      }

    sealed trait Command

    case class Create(name: String) extends Command

    case class Proxy(message: String, sendTo: ActorRef[String]) extends Command

    case class Schedule(name: String) extends Command

    case object Done extends Command
  }
}
