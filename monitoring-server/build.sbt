scalaVersion := "2.13.6"
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
      monitoring,
      equipmentApi,
      sessionApi
    )

lazy val common = project
    .in(file("common"))
    .settings(
      name := "common",
      settings,
      libraryDependencies ++= commonDependencies ++ Seq(
        "com.github.jwt-scala" %% "jwt-circe" % "9.0.1"
      )
    )
    .disablePlugins(AssemblyPlugin)

lazy val gateway = project
    .in(file("gateway-service"))
    .settings(
      name := "gateway-service",
      settings,
      libraryDependencies ++= commonDependencies
    )
    .dependsOn(common, equipmentApi)

lazy val session = project
    .in(file("session-service"))
    .settings(
      name := "session-service",
      settings,
      libraryDependencies ++= commonDependencies
    )
    .dependsOn(common, sessionApi)

lazy val reporting = project
    .in(file("reporting-service"))
    .settings(
      name := "reporting-service",
      settings,
      libraryDependencies ++= commonDependencies
    )
    .dependsOn(common)

lazy val equipment = project
    .in(file("equipment-service"))
    .settings(
      name := "equipment-service",
      settings,
      libraryDependencies ++= commonDependencies
    )
    .dependsOn(common, equipmentApi)

lazy val documentation = project
    .in(file("documentation-service"))
    .settings(
      name := "documentation-service",
      settings,
      libraryDependencies ++= commonDependencies
    )
    .dependsOn(common)

lazy val generator = project
    .in(file("generator-service"))
    .settings(
      name := "generator-service",
      settings,
      libraryDependencies ++= commonDependencies
    )
    .dependsOn(common)

lazy val monitoring = project
    .in(file("monitoring-service"))
    .settings(
      name := "monitoring-service",
      settings,
      libraryDependencies ++= commonDependencies
    )
    .dependsOn(common)

lazy val equipmentApi = project
    .in(file("equipment-service-api"))
    .settings(
      name := "equipment-service-api",
      settings,
      libraryDependencies ++= commonDependencies
    )
    .dependsOn(common)
    .disablePlugins(AssemblyPlugin)

lazy val sessionApi = project
    .in(file("session-service-api"))
    .settings(
      name := "session-service-api",
      settings,
      libraryDependencies ++= commonDependencies
    )
    .dependsOn(common)
    .disablePlugins(AssemblyPlugin)

lazy val commonDependencies = Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
  "com.typesafe.slick" %% "slick" % "3.3.3",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
  "org.postgresql" % "postgresql" % "42.2.23.jre7",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.lambdista" %% "config" % "0.7.1",
  "com.github.finagle" %% "finchx-core" % "0.32.1",
  "com.github.finagle" %% "finchx-circe" % "0.32.1",
  "com.github.finagle" % "finagle-http-auth_2.12" % "0.1.0",
  "io.circe" %% "circe-generic" % "0.14.1",
  "io.circe" %% "circe-json-schema" % "0.2.0",
  "io.circe" % "circe-literal_2.12" % "0.15.0-M1",
  "com.twitter" %% "twitter-server" % "21.8.0",
  "com.rms.miu" %% "slick-cats" % "0.10.4",
  "com.beachape" %% "enumeratum-circe" % "1.7.0",
  "com.beachape" %% "enumeratum-slick" % "1.7.0"
)

lazy val settings = Seq(
  scalacOptions ++= compilerOptions,
  resolvers += "jitpack".at("https://jitpack.io")
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
