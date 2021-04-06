import scala.sys.process._

lazy val commonSettings = Seq(
  organization := "io.pig",
  scalaVersion := "2.13.5",
  version := "0.1.0-SNAPSHOT"
)

lazy val posts = (project in file("posts"))
  .enablePlugins(MdocPlugin)
  .settings(
    commonSettings,
    mdocIn := file("posts"),
    mdocOut := file("docs"),
    mdoc := {
      mdoc.evaluated
      "web/process-md.sh" !
    }
  )
