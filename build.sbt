ThisBuild / scalaVersion := "2.12.10"
ThisBuild / organization := "org.demuinck"

val flinkV = "1.13.0"


lazy val root = (project in file("."))
  .settings(
    name := "SnowplowAggregate",
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
