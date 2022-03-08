ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.8"

lazy val dependencies = libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % "2.6.18",
  "ch.qos.logback"     % "logback-classic"  % "1.2.10"
)

lazy val hello = project
  .in(file("chapter_01/01_hello_world"))
  .withId("hello")
  .settings(dependencies)

lazy val types = project
  .in(file("chapter_01/02_proper_types"))
  .settings(dependencies)

lazy val state = project
  .in(file("chapter_01/03_state"))
  .settings(dependencies)

lazy val newActors = project
  .in(file("chapter_01/04_creating_new_actors"))
  .withId("newActors")
  .settings(dependencies)

lazy val fsm = project
  .in(file("chapter_01/05_fsm"))
  .withId("fsm")
  .settings(dependencies)

lazy val ask = project
  .in(file("chapter_01/06_ask"))
  .withId("ask")
  .settings(dependencies)

lazy val askWithPayload = project
  .in(file("chapter_01/07_ask_with_payload"))
  .withId("askWithPayload")
  .settings(dependencies)
