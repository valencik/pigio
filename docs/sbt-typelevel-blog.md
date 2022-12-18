Blogging With sbt-typelevel
===========================

> Andrew, this isn't a blog, it's like a documentation site for a non-existant library

 Well, yeah, sorta.
 But the thing is, I want to have some scala code here and [sbt-typelevel] just takes care of everything for me these days.
 Look!

```scala mdoc
val name = "Blog"

s"Hi $name, it's Andrew writing Scala!"
```

> Thrilling


## Setup

To use [sbt-typelevel] as a static blog you'll likely want to set it up slightly differently from a regular library.

I started with the following `project/plugins.sbt`:

```scala
addSbtPlugin("org.typelevel" % "sbt-typelevel-site" % "0.5.0-M8")
addSbtPlugin("org.typelevel" % "sbt-typelevel-settings" % "0.5.0-M8")
```

You can get by with just `sbt-typelevel-site`, which will give you the `docs/tlSitePreview` command to build and preview you site.
But adding `sbt-typelevel-settings` is nice as it sets good scalac options even for your mdoc build.


And in order to disable publishing artifacts altogether you'll want to doing something like the following in your `build.sbt`:

```scala
ThisBuild / githubWorkflowPublishTargetBranches := Seq()
```

Finally, remember to actually enable GitHub Pages in your repo, it's in Settings > Pages.
I always forget this step.


[sbt-typelevel]: https://typelevel.org/sbt-typelevel/index.html
