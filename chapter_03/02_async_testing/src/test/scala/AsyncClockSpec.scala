import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import akka.actor.testkit.typed.scaladsl.ManualTime
import akka.actor.testkit.typed.scaladsl.TestProbe

import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import scala.concurrent.duration._

class AsyncClockSpec extends ScalaTestWithActorTestKit(ManualTime.config) with AnyWordSpecLike with Matchers {

  val manualTime: ManualTime = ManualTime()

  "Manual Time" must {

    "redirect to Reader the content of the messages received" in {
      val exchanger = testKit.spawn(SwapDelayed(), "cookie") // create -> monitor
      val probe     = TestProbe[String]("counter")
      exchanger ! SwapDelayed.Take(probe.ref, "stuff")
      manualTime.expectNoMessageFor(99.millis, probe)
      manualTime.timePasses(2.millis)
      probe.expectMessage("stuff".reverse)
      manualTime.expectNoMessageFor(1.seconds, probe)

    }
  }
}

object SwapDelayed {
  def apply(): Behavior[Command] = Behaviors.receiveMessage { case Take(ref, message) =>
    doing(1000)
    ref ! message.reverse
    Behaviors.same
  }

  private def doing(duration: Int): Unit = {
    val endTime = System.currentTimeMillis + duration
    while (endTime > System.currentTimeMillis) {}
  }

  sealed trait Command
  case class Take(ref: ActorRef[String], str: String) extends Command
}
