import com.mojolly.scalate.ScalatePlugin.ScalateKeys._
import com.mojolly.scalate.ScalatePlugin._
import org.scalatra.sbt._
import sbt.Keys._
import sbt._
import play.api.libs.json._

object RacoonbotBuild extends Build {
  val Organization = "operdeni"
  val Name = "racoonbot"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.11.6"
  val ScalatraVersion = "2.4.0-RC2-2"

  lazy val project = Project (
    "racoonbot",
    file("."),
    settings = seq(com.typesafe.sbt.SbtStartScript.startScriptForClassesSettings: _*
      ) ++ ScalatraPlugin.scalatraSettings ++ scalateSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
        "org.eclipse.jetty" % "jetty-client" % "9.2.10.v20150310",
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
        "ch.qos.logback" % "logback-classic" % "1.1.2" % "runtime",
        "org.eclipse.jetty" % "jetty-webapp" % "9.2.10.v20150310" % "compile;container",
        "javax.servlet" % "javax.servlet-api" % "3.1.0" % "compile;provided",
        "org.scalatra" %% "scalatra-json" % ScalatraVersion,
        "org.json4s"   %% "json4s-jackson" % "3.3.0.RC1",
        "org.scalaj" %% "scalaj-http" % "1.1.5"
      ),
      dependencyOverrides := Set(
        "org.scala-lang" % "scala-library" % ScalaVersion,
        "org.scala-lang" % "scala-reflect" % ScalaVersion,
        "org.scala-lang" % "scala-compiler" % ScalaVersion
      ),
      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile){ base =>
        Seq(
          TemplateConfig(
            base / "webapp" / "WEB-INF" / "templates",
            Seq.empty,  /* default imports should be added here */
            Seq(
              Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
            ),  /* add extra bindings here */
            Some("templates")
          )
        )
      }
    )
  )
}
