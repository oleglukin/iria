import Dependencies._

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.oleglukin"
ThisBuild / organizationName := "oleglukin"

lazy val root = (project in file("."))
  .settings(
    name := "iria",
    libraryDependencies += scalaTest % Test
  )

// Uncomment the following for publishing to Sonatype.
// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for more detail.

// ThisBuild / description := "Some descripiton about your project."
// ThisBuild / licenses    := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt"))
ThisBuild / homepage    := Some(url("https://github.com/oleglukin/iria"))
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/oleglukin/iria"),
    "scm:git@github.com:oleglukin/iria.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id    = "oleglukin",
    name  = "Oleg Lukin",
    email = "oleg.e.lukin@hotmail.com",
    url   = url("https://github.com/oleglukin")
  )
)
// ThisBuild / pomIncludeRepository := { _ => false }
// ThisBuild / publishTo := {
//   val nexus = "https://oss.sonatype.org/"
//   if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
//   else Some("releases" at nexus + "service/local/staging/deploy/maven2")
// }
// ThisBuild / publishMavenStyle := true
