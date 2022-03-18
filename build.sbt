ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.8"

lazy val dependencies = libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % "2.6.18",
  "ch.qos.logback"     % "logback-classic"  % "1.2.10"
)

lazy val hello = project
  .in(file("chapter_02/01_hello_world"))
  .withId("hello")
  .settings(dependencies)

lazy val types = project
  .in(file("chapter_02/02_proper_types"))
  .settings(dependencies)

lazy val state = project
  .in(file("chapter_02/03_state"))
  .settings(dependencies)

lazy val newActors = project
  .in(file("chapter_02/04_creating_new_actors"))
  .withId("newActors")
  .settings(dependencies)

lazy val fsm = project
  .in(file("chapter_02/05_fsm"))
  .withId("fsm")
  .settings(dependencies)

lazy val ask = project
  .in(file("chapter_02/06_ask"))
  .withId("ask")
  .settings(dependencies)

lazy val askWithPayload = project
  .in(file("chapter_02/07_ask_with_payload"))
  .withId("askWithPayload")
  .settings(dependencies)

lazy val timers = project
  .in(file("chapter_02/08_timers"))
  .withId("timers")
  .settings(dependencies)

lazy val sync = project
  .in(file("chapter_03/01_sync_testing"))
  .withId("sync")
  .settings(
    dependencies,
    libraryDependencies ++= Seq(
      "org.scalatest"     %% "scalatest"                % "3.2.11" % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.6.18" % Test
    )
  )

lazy val async = project
  .in(file("chapter_03/02_async_testing"))
  .withId("async")
  .settings(
    dependencies,
    libraryDependencies ++= Seq(
      "org.scalatest"     %% "scalatest"                % "3.2.11" % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.6.18" % Test
    )
  )
