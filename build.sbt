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
