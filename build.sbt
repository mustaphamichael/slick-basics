name := "slick-basics"

version := "0.1"

scalaVersion := "2.13.3"

val FlywayVersion = "7.14.0"
val PostgresVersion = "42.2.18"
val SlickVersion = "3.3.3"

libraryDependencies ++= Seq(
  "com.typesafe.slick"   %% "slick"               % SlickVersion,
  "org.postgresql"        % "postgresql"          % PostgresVersion,
  "org.flywaydb"          % "flyway-core"         % FlywayVersion,
)
