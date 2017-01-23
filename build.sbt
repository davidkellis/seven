// modeled this build.sbt off of http://www.typesafe.com/activator/template/scala-library-seed#code/build.sbt

mainClass in assembly := Some("davidkellis.seven.Main")

name := "seven"

version := "1.0.0"

scalaVersion := "2.12.1"

libraryDependencies  ++= Seq(
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",

  "org.log4s" %% "log4s" % "1.3.4",

  "org.threeten" % "threeten-extra" % "1.0",

  "org.ehcache" % "ehcache" % "3.2.0",

  "org.rogach" %% "scallop" % "2.0.6",
  "com.typesafe" % "config" % "1.3.1",

  "org.scalikejdbc" %% "scalikejdbc" % "2.5.0",
  "org.scalikejdbc" %% "scalikejdbc-config"  % "2.5.0",
  "org.postgresql" % "postgresql" % "9.4.1212",
  "com.zaxxer" % "HikariCP" % "2.5.1"

//  "org.json4s" %% "json4s-jackson" % "3.2.9",
//  "com.lambdaworks" % "jacks_2.11" % "2.3.3",
//  "net.sandrogrzicic" %% "scalabuff-runtime" % "1.3.8",
//  "org.scala-lang" % "scala-compiler" % "2.11.0",
//  "org.scala-lang" % "jline" % "2.11.0-M3",
//  "org.spire-math" % "spire_2.10" % "0.7.4",
//  "org.scalanlp" % "breeze_2.10" % "0.7",
//  "org.scalanlp" % "nak" % "1.2.1"
)

resolvers ++= Seq(
  // other resolvers here
  // if you want to use snapshot builds (currently 0.12-SNAPSHOT), use this.
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
)

// bintray stuff
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
organization := "com.github.davidkellis"

//scalacOptions ++= Seq(
//  "-deprecation",
//  "-unchecked",
//  "-Xelide-below", "INFO",
//  "-feature",
//  "-language", "implicitConversions"
//)
