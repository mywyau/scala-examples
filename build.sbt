import sbt.Keys.libraryDependencies

ThisBuild / scalaVersion := "2.12.10"

lazy val hello = (project in file("."))
  .settings(

    name := "scala-exmaples",

    libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test,
    libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.9",
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0",
    scalacOptions += "-Ypartial-unification"

  )






