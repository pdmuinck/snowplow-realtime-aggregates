
lazy val settings =
  commonSettings ++ scalafmtSettings

lazy val compilerOptions = Seq(
  "-encoding",
  "utf8",
  "-unchecked",
  "-deprecation"
)

lazy val commonSettings = Seq(scalacOptions ++= compilerOptions)

lazy val scalafmtSettings =
  Seq(
    scalafmtOnCompile := true,
    scalafmtTestOnCompile := true,
    scalafmtVersion := "1.2.0"
  )

lazy val assemblySettings = Seq(
  assembly / assemblyMergeStrategy := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case "application.conf"            => MergeStrategy.concat
    case _                             => MergeStrategy.first
  },
  // exclude Scala library from assembly
  assembly / assemblyOption := (assembly / assemblyOption).value
    .copy(includeScala = true),
  assembly / test := {}
)

// make run command include the provided dependencies
Compile / run := Defaults
  .runTask(Compile / fullClasspath, Compile / run / mainClass, Compile / run / runner)
  .evaluated

Global / cancelable := true