scalaVersion := "2.12.14"
name := "monitoring-server"
organization := "com.qwerfah"
version := "1.0"

lazy val global = project
    .in(file("."))
    .settings(settings)
    .disablePlugins(AssemblyPlugin)
    .aggregate(
      common,
      gateway,
      session,
      reporting,
      equipment,
      documentation,
      generator,
      monitoring
    )

lazy val common = project
    .in(file("common-service"))
    .settings(
      name := "common-service",
      settings,
      libraryDependencies ++= commonDependencies
    )
    .disablePlugins(AssemblyPlugin)

lazy val gateway = project
    .in(file("gateway-service"))
    .settings(
      name := "gateway-service",
      settings,
      libraryDependencies ++= commonDependencies
    )
    .dependsOn(
      common
    )

lazy val session = project
    .in(file("session-service"))
    .settings(
      name := "session-service",
      settings,
      libraryDependencies ++= commonDependencies
    )
    .dependsOn(
      common
    )

lazy val reporting = project
    .in(file("reporting-service"))
    .settings(
      name := "reporting-service",
      settings,
      libraryDependencies ++= commonDependencies
    )
    .dependsOn(
      common
    )

lazy val equipment = project
    .in(file("equipment-service"))
    .settings(
      name := "equipment-service",
      settings,
      libraryDependencies ++= commonDependencies
    )
    .dependsOn(
      common
    )

lazy val documentation = project
    .in(file("documentation-service"))
    .settings(
      name := "documentation-service",
      settings,
      libraryDependencies ++= commonDependencies
    )
    .dependsOn(
      common
    )

lazy val generator = project
    .in(file("generator-service"))
    .settings(
      name := "generator-service",
      settings,
      libraryDependencies ++= commonDependencies
    )
    .dependsOn(
      common
    )

lazy val monitoring = project
    .in(file("monitoring-service"))
    .settings(
      name := "monitoring-service",
      settings,
      libraryDependencies ++= commonDependencies
    )
    .dependsOn(
      common
    )

lazy val commonDependencies = Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
  "com.typesafe.slick" %% "slick" % "3.3.3",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
  "org.postgresql" % "postgresql" % "42.2.23.jre7",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.lambdista" %% "config" % "0.7.1",
  "com.github.finagle" %% "finchx-core" % "0.32.1",
  "com.github.finagle" %% "finchx-circe" % "0.32.1",
  "io.circe" %% "circe-generic" % "0.14.1",
  "com.twitter" %% "twitter-server" % "21.8.0",
  "com.rms.miu" %% "slick-cats" % "0.10.4"
)

lazy val settings = Seq(
  scalacOptions ++= compilerOptions
)

lazy val compilerOptions = Seq(
  "-unchecked",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-deprecation",
  "-encoding",
  "utf8"
)
