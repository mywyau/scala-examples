import sbt.Keys.libraryDependencies

ThisBuild / scalaVersion := "2.13.3"

autoCompilerPlugins := true

val kindProjector = addCompilerPlugin("org.typelevel" % "kind-projector" % "0.11.3" cross CrossVersion.full) // doesnt work wip maybe

addCompilerPlugin("org.typelevel" % "kind-projector" % "0.11.3" cross CrossVersion.full) // doesnt work wip maybe

libraryDependencies ++= (scalaBinaryVersion.value match { // doesnt work wip
  case "2.10" =>
    compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full) :: Nil
  case _ =>
    Nil
})


lazy val hello = (project in file("."))
  .settings(

    name := "scala-notes-and-examples",

    libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.10",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % "test",
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.3.0",
    //    scalacOptions += "-Ypartial-unification",
    scalacOptions += "-language:higherKinds", // for higher kinds
    kindProjector,
    //    libraryDependencies ++= Seq("org.scalatest" %% "scalatest" % "3.0.1" % "test", "org.mockito" % "mockito-core" % "2.8.47" % "test"),
    //    on or the other please for now with mockito or scala mock not sure why wip
    libraryDependencies += "org.scalamock" %% "scalamock" % "5.1.0" % "test",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % "test",
    libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.1" % "test" //property testing
  )

lazy val testSettings: Seq[Def.Setting[_]] = Seq(
  fork := true,
  javaOptions ++= Seq(
    "-Dconfig.resource=test.application.conf",
    "-Dlogger.resource=logback-test.xml"
  )
)





