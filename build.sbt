ThisBuild / organization := "io.pig"
ThisBuild / organizationName := "Pig.io"
ThisBuild / startYear := Some(2022)
ThisBuild / licenses := Seq(License.Apache2)
ThisBuild / developers := List(
  tlGitHubDev("valencik", "Andrew Valencik")
)

// publish website from this branch
ThisBuild / tlSitePublishBranch := Some("main")

// do not publish artifacts
ThisBuild / githubWorkflowPublishTargetBranches := Seq()

// use JDK 11
ThisBuild / githubWorkflowJavaVersions := Seq(JavaSpec.temurin("11"))

val Scala213 = "2.13.10"
ThisBuild / crossScalaVersions := Seq(Scala213)
ThisBuild / scalaVersion := Scala213 // the default Scala

import laika.ast.Path.Root
import laika.helium.config.{IconLink, HeliumIcon, TextLink, ThemeNavigationSection}
import cats.data.NonEmptyList
lazy val docs = project
  .in(file("site"))
  .enablePlugins(TypelevelSitePlugin)
  .settings(
    tlSiteHelium := {
      tlSiteHelium.value.site.darkMode.disabled.site
        .topNavigationBar(
          homeLink = IconLink.external("https://github.com/valencik", HeliumIcon.home)
        )
    }
  )
