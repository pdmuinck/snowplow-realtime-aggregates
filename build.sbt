ThisBuild / scalaVersion := "2.12.10"
ThisBuild / organization := "io.github.pdmuinck"
ThisBuild / name := "snowplow-flink-aggregates"
ThisBuild / version := "0.1"
ThisBuild / homepage := Some(url("https://github.com/pdmuinck/snowplow-realtime-aggregates"))
ThisBuild / scmInfo := Some(ScmInfo(url("https://github.com/pdmuinck/snowplow-realtime-aggregates"), "git@github.com:pdmuinck/snowplow-realtime-aggregates.git"))
ThisBuild / developers := List(Developer("pdmuinck", "pdmuinck", "pieterjandem@live.be", url("https://github.com/pdmuinck")))
ThisBuild / licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
ThisBuild / publishMavenStyle := true
ThisBuild / crossPaths := false

ThisBuild / publishTo := {
  val nexus = "https://s01.oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}


val flinkV = "1.13.0"


lazy val root = (project in file("."))
  .settings(
    name := "snowplow-flink-aggregates",
     libraryDependencies ++= Seq(
      "org.apache.flink"           %% "flink-clients"                 % flinkV,
      "org.apache.flink"           %% "flink-scala"                   % flinkV,
      "org.apache.flink"           %% "flink-streaming-scala"         % flinkV,
      "org.apache.flink"           %% "flink-test-utils"              % flinkV % "test",
      "org.apache.flink"           %% "flink-runtime"                 % flinkV % "test" classifier "tests",
      "org.apache.flink"           %% "flink-streaming-java"          % flinkV % "test" classifier "tests",
      "org.scalatest"              %% "scalatest"                     % "3.2.7" % Test,
      "com.snowplowanalytics"      %% "snowplow-scala-analytics-sdk"  % "2.1.0",
      "com.typesafe.scala-logging" %% "scala-logging"                 % "3.9.4"
    )
  )
