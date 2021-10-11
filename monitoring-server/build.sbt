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
      gatewayApi,
      equipmentApi,
      documentationApi,
      monitoringApi,
      generatorApi,
      reportingApi
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
      dockerBaseImage := "openjdk",
      libraryDependencies ++= commonDependencies,
      dockerExposedPorts ++= Seq(8081, 8081)
    )
    .dependsOn(
      common,
      gatewayApi,
      equipmentApi,
      documentationApi,
      monitoringApi,
      generatorApi,
      reportingApi
    )
    .enablePlugins(JavaAppPackaging, DockerPlugin)

lazy val session = project
    .in(file("session-service"))
    .settings(
      name := "session-service",
      settings,
      dockerBaseImage := "openjdk",
      libraryDependencies ++= commonDependencies,
      dockerExposedPorts ++= Seq(8082, 8082)
    )
    .dependsOn(common)
    .enablePlugins(JavaAppPackaging, DockerPlugin)

lazy val equipment = project
    .in(file("equipment-service"))
    .settings(
      name := "equipment-service",
      settings,
      dockerBaseImage := "openjdk",
      libraryDependencies ++= commonDependencies,
      dockerExposedPorts ++= Seq(8083, 8083)
    )
    .dependsOn(common, equipmentApi, monitoringApi, documentationApi)
    .enablePlugins(JavaAppPackaging, DockerPlugin)

lazy val documentation = project
    .in(file("documentation-service"))
    .settings(
      name := "documentation-service",
      settings,
      dockerBaseImage := "openjdk",
      libraryDependencies ++= commonDependencies,
      dockerExposedPorts ++= Seq(8084, 8084)
    )
    .dependsOn(common, documentationApi)
    .enablePlugins(JavaAppPackaging, DockerPlugin)

lazy val monitoring = project
    .in(file("monitoring-service"))
    .settings(
      name := "monitoring-service",
      settings,
      dockerBaseImage := "openjdk",
      libraryDependencies ++= commonDependencies,
      dockerExposedPorts ++= Seq(8085, 8085)
    )
    .dependsOn(common, monitoringApi, equipmentApi, generatorApi)
    .enablePlugins(JavaAppPackaging, DockerPlugin)

lazy val generator = project
    .in(file("generator-service"))
    .settings(
      name := "generator-service",
      settings,
      dockerBaseImage := "openjdk",
      libraryDependencies ++= commonDependencies,
      dockerExposedPorts ++= Seq(8086, 8086)
    )
    .dependsOn(common, generatorApi, equipmentApi)
    .enablePlugins(JavaAppPackaging, DockerPlugin)

lazy val reporting = project
    .in(file("reporting-service"))
    .settings(
      name := "reporting-service",
      settings,
      dockerBaseImage := "openjdk",
      libraryDependencies ++= commonDependencies,
      dockerExposedPorts ++= Seq(8087, 8087)
    )
    .dependsOn(common, reportingApi)
    .enablePlugins(JavaAppPackaging, DockerPlugin)

lazy val gatewayApi = project
    .in(file("gateway-service-api"))
    .settings(
      name := "gateway-service-api",
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

lazy val documentationApi = project
    .in(file("documentation-service-api"))
    .settings(
      name := "documentation-service-api",
      settings,
      libraryDependencies ++= commonDependencies
    )
    .dependsOn(common)
    .disablePlugins(AssemblyPlugin)

lazy val monitoringApi = project
    .in(file("monitoring-service-api"))
    .settings(
      name := "monitoring-service-api",
      settings,
      libraryDependencies ++= commonDependencies
    )
    .dependsOn(common)
    .disablePlugins(AssemblyPlugin)

lazy val generatorApi = project
    .in(file("generator-service-api"))
    .settings(
      name := "generator-service-api",
      settings,
      libraryDependencies ++= commonDependencies
    )
    .dependsOn(common)
    .disablePlugins(AssemblyPlugin)

lazy val reportingApi = project
    .in(file("reporting-service-api"))
    .settings(
      name := "reporting-service-api",
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
  "com.lambdista" %% "config" % "0.8.1",
  "com.github.finagle" %% "finchx-core" % "0.32.1",
  "com.github.finagle" %% "finchx-circe" % "0.32.1",
  "com.twitter" %% "twitter-server" % "21.8.0",
  "io.circe" %% "circe-generic" % "0.15.0-M1",
  "io.circe" %% "circe-json-schema" % "0.2.0",
  "io.circe" % "circe-literal_2.13" % "0.15.0-M1",
  "io.circe" % "circe-config_2.13" % "0.8.0",
  "com.rms.miu" %% "slick-cats" % "0.10.4",
  "com.beachape" %% "enumeratum-circe" % "1.7.0",
  "com.beachape" %% "enumeratum-slick" % "1.7.0",
  "io.catbird" % "catbird-util_2.13" % "21.5.0",
  "org.slf4j" % "slf4j-api" % "2.0.0-alpha5",
  "ch.qos.logback" % "logback-core" % "1.3.0-alpha10",
  "ch.qos.logback" % "logback-classic" % "1.3.0-alpha10",
  "com.github.pjfanning" %% "op-rabbit-circe" % "2.6.3",
  "com.github.pjfanning" %% "op-rabbit-core" % "2.6.3",
  "com.github.pjfanning" %% "op-rabbit-akka-stream" % "2.6.3"
)

lazy val settings = Seq(
  scalacOptions ++= compilerOptions,
  ThisBuild / resolvers += "jitpack".at("https://jitpack.io")
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
