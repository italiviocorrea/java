name := "api-ncm-reactive"

version := "1.0"


scalaVersion := "2.13.1"

scalacOptions += "-Ypartial-unification" // 2.11.9+

libraryDependencies ++= {

  lazy val doobieVersion = "0.8.4"
  lazy val http4sVersion = "0.21.4"
  lazy val circeVersion = "0.13.0".


  Seq(
    "org.tpolecat"          %% "doobie-core"            % doobieVersion,
    "org.tpolecat"          %% "doobie-h2"              % doobieVersion,
    "org.tpolecat"          %% "doobie-hikari"          % doobieVersion,
    "org.tpolecat"          %% "doobie-postgres"        % doobieVersion,          // Postgres driver 42.2.9 + type mappings.
    "org.tpolecat"          %% "doobie-quill"           % doobieVersion,
    "org.tpolecat"          %% "doobie-specs2"          % doobieVersion,
    "org.http4s"            %% "http4s-blaze-server"    % http4sVersion,
    "org.http4s"            %% "http4s-circe"           % http4sVersion,
    "org.http4s"            %% "http4s-dsl"             % http4sVersion,
    "io.circe"              %% "circe-core"             % circeVersion,
    "io.circe"              %% "circe-generic"          % circeVersion,
    "io.circe"              %% "circe-config"           % "0.7.0",
    "org.slf4j"             % "slf4j-api"               % "1.7.5",
    "ch.qos.logback"        % "logback-classic"         % "1.0.9"
  )

}

resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
)