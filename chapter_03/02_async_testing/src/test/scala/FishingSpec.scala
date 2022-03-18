import akka.actor.testkit.typed.scaladsl._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import akka.actor.typed.scaladsl.{Behaviors, TimerScheduler}
import akka.actor.typed.{ActorRef, Behavior}

import scala.concurrent.duration._

class FishingSpec extends ScalaTestWithActorTestKit with AnyWordSpecLike with Matchers {

  "A timing test" must {

    import Forward._

    val interval = 1.seconds

    "be able to cancel timer" in {
      val probe = TestProbe[Event]("evt")
      val behv = Behaviors.withTimers[Command] { timer =>
        timer.startTimerWithFixedDelay(TimerKey, Tick(1), interval)
        apply(probe.ref, timer)
      }

      val ref = spawn(behv)
      probe.expectMessage(Tock(1))
      ref ! Cancel
      probe.fishForMessage(3.seconds) {
        // we don't know that we will see exactly one tock
        case _: Tock => FishingOutcomes.continueAndIgnore
        // but we know that after we saw Cancelled we won't see any more
        case Cancelled => FishingOutcomes.complete
        case message   => FishingOutcomes.fail(s"unexpected message: $message")
      }
      probe.expectNoMessage(interval + 100.millis.dilated)
    }
  }
}

object Forward {

  sealed trait Command
  case class Tick(n: Int) extends Command
  case object Cancel      extends Command

  sealed trait Event
  case class Tock(n: Int) extends Event
  case object Cancelled   extends Event

  case object TimerKey

  def apply(forwardTo: ActorRef[Event], timer: TimerScheduler[Command]): Behavior[Command] = {
    Behaviors
      .receiveMessage[Command] {
        case Tick(n) =>
          forwardTo ! Tock(n)
          Behaviors.same
        case Cancel =>
          timer.cancel(TimerKey)
          forwardTo ! Cancelled
          Behaviors.same
      }
  }
}
