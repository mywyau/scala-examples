ThisBuild / scalaVersion := "2.12.7"

lazy val hello = (project in file("."))
  .settings(
    name := "scala-exmaples",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test,
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.9",
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0",
    scalacOptions += "-Ypartial-unification"


  )

//name := "scala-exmaples"
//
//version := "0.1"
//
//scalaVersion := "2.13.3"
