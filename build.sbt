import sbt.Keys.libraryDependencies

ThisBuild / scalaVersion := "2.12.10"

autoCompilerPlugins := true

val kindProjector = addCompilerPlugin("org.typelevel" % "kind-projector" % "0.11.3" cross CrossVersion.full)  // kinda work wip
addCompilerPlugin("org.typelevel" % "kind-projector" % "0.11.3" cross CrossVersion.full)  // kinda work wip

libraryDependencies ++= (scalaBinaryVersion.value match {   // kinda work wip
  case "2.10" =>
    compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full) :: Nil
  case _ =>
    Nil
})

lazy val hello = (project in file("."))
  .settings(

    name := "scala-notes-and-examples",

    libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test,
    libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.9",
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0",
    scalacOptions += "-Ypartial-unification",
    scalacOptions += "-language:higherKinds", // for higher kinds
    kindProjector
  )


lazy val testSettings: Seq[Def.Setting[_]] = Seq(
  fork := true,
  javaOptions ++= Seq(
    "-Dconfig.resource=test.application.conf",
    "-Dlogger.resource=logback-test.xml"
  )
)





