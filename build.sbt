import sbt.Keys.libraryDependencies

ThisBuild / organization := "mikey corp"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.8"

autoCompilerPlugins := true

val kindProjector = addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.13.2" cross CrossVersion.full) // doesnt work wip maybe

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.13.2" cross CrossVersion.full) // doesnt work wip maybe


lazy val root = (project in file("."))
  .settings(
    name := "scala-notes-and-examples",
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.1",
      "org.scalactic" %% "scalactic" % "3.2.14",
      "org.typelevel" %% "cats-core" % "2.9.0",
      "org.scalatest" %% "scalatest" % "3.2.14" % Test,
      "com.disneystreaming" %% "weaver-cats" % "0.8.1" % Test,  //disney streaming
      "org.scalamock" %% "scalamock" % "5.2.0" % Test,
      "org.scalatest" %% "scalatest" % "3.2.14" % Test,
      "org.scalacheck" %% "scalacheck" % "1.17.0" % Test //property testing
    ),
    scalacOptions ++=
      Seq("-language:higherKinds"),
      kindProjector
  )

lazy val testSettings: Seq[Def.Setting[_]] = {
  Seq(
    fork := true,
    javaOptions ++= Seq(
      "-Dconfig.resource=test.application.conf",
      "-Dlogger.resource=logback-test.xml"
    )
  )
}





