ThisBuild / version := "1.0.0"

ThisBuild / scalaVersion := "2.13.10"

val logbackVersion = "1.3.4"
val sfl4sVersion = "2.0.3"
val akkaVersion = "2.5.26"
val akkaHttpVersion = "10.1.11"
val scalatestVersion = "3.2.7"


libraryDependencies ++= Seq(

  "org.scalatest" %% "scalatest" % scalatestVersion,
  "org.scalatest" %% "scalatest" % scalatestVersion % "test",
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "ch.qos.logback" % "logback-core" % logbackVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "org.slf4j" % "slf4j-api" % sfl4sVersion,
  "com.typesafe" % "config" % "1.4.2",
  "org.scala-lang" % "scala-reflect" % "2.13.10",
  "com.softwaremill.sttp.client3" %% "core" % "3.1.9",
  "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
  "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion,

)

Compile / PB.targets := Seq(
  scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
)


