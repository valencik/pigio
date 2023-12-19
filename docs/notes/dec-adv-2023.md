## December Adventure 2023

### Dec. 1

### Dec. 2
Continued working on Laika integration in an sbt plugin.
Actually use the `IndexFormat` now, so we're really writing an Index file.

### Dec. 3
Tweaking `PlaintextRenderer` so we don't get a bunch of lines smushed together.

### Dec. 4
Considerably improving `PlaintextRenderer`.

### Dec. 5
Paired with Sam on protosearch.
Learned (from Sam) that we can temporarily hack the sbt build to get the same version output with something like:

```scala
ThisBuild / version := "0.0-sbt-SNAPSHOT"
```

This helped speed up the iteration loop as we're testing by running the plugin on the http4s build.

We're actually writing the protosearch.js to the jar now, and things are getting close.

### Dec. 6
Switched up how we write the helper files to use Laika's `addClassLoaderResource`.
Make the `protosearchGenerateIndex` run after `laikaSite`.
We can now add a single plugin line to an sbt project setup with sbt-typelevel and get a search page!

The search page is really rough still, so we tweak some UX a bit, but the real fix requires supporting stored fields.

### Dec. 7
Opened an issue for [supporting stored fields][protosearch-stored].
We want this is order to nicely show metadata about a document like its title, a text preview, etc.

We're trying to improve the sbt plugin setup so that we're not overwriting the `laikaSite` task.
This proves very difficult.
But eventually Sam comes through with the genius move:

```scala
protosearchGenerateIndex := Tasks.protosearchGenerateIndex.triggeredBy(laikaSite).value
```

It works wonderfully.

### Dec. 8

### Dec. 9

### Dec. 10
[Opened an issue][protosearch-binary] about the design of a binary index format for protosearch.

Updated the protosearch sbt plugin PR description. It's merge ready.

### Dec. 11
<!-- Monday. nothing? -->

### Dec. 12
I worked on [MUnit][munit] today.
Opening a PR to [drop Scala 2.11 support][drop-211], which should help us catch up on ScalaJS versions more easily.
And a PR to restore the v0.7.x series [fixture ordering][fixture-ordering].
This has been a TODO item for me for way too long, I'm happy to see it done.
In my opinion it's the last blocking change for a v1.0 release.

### Dec. 13
Learned that Elasticsearch doesn't require a different field type if a value is a single string or a array of strings.

### Dec. 14
Learning about wasm and in particular that duckdb-wasm exists.

Nerd sniped on using cats-parse to tackle Advent of Code day 1 part two.
Writing parsers for each of the numeric words (eg. `one`, `two`) was easy enough.
Similarly with looking out for junk input on either side.
The trick was handling overlaping words (eg. `twone` is two and one).
I ended up using `Parser#peek` in something like this:

```scala
//> using toolkit typelevel:latest
import cats.parse.{Parser => P}
import cats.parse.Rfc5234.alpha

def num(str: String, num: Int): P[Int] =
  (P.string(str.take(str.size - 1)).soft *> P.char(str.last).peek).as(num)

val nums = P.oneOf(num("one", 1) :: num("two", 2) :: Nil)
val parseNums = (nums.map(Some(_)) | (alpha.repUntil(nums) *> nums.?)).rep
def process(str: String) = parseNums.parseAll(str).map(_.toList.flatten)

println(process("zzztwonexxx"))
```

### Dec. 15
[Played with smithy4s][learn-smithy] some more.
Confirmed I can easily build an endpoint to match an existing API's shape with either `@httpQueryParams` or `@httpQuery` and "list of strings" shape.

Dug out an old book, Graphics File Format 2e from 1996, with the intent to learn more about other binary formats to help figure out an approach for protosearch.

### Dec. 16
Learning more about the GIF format, and in particular how it handles the Application data extensions.
It turns out most implementations ignore the Plaintext extension.
However the Application data extension is used to signal that a GIF should loop.
Started learning more about LZW compression, it seems like it might not be too hard to implement.

Also discovered the AudioKit Synth One Synthesizer, which was wonderful to spend the rest of the day with.

### Dec. 17
Fixed the codec for the `IndexWithFields` prototype.
Put up a [draft PR][protosearch-stored-pr] with initial support for stored fields.

Paired with Sam on some upcoming Typelevel documentation.

### Dec. 18
Paired with Arman and Sam on a Grackle security release.
We had to do this manually, which has given me a new appreciation for how much our current [sbt-typelevel][sbt-typelevel] automates for us.

Finished to first simple implementation of a printer for [Lucille][lucille].

### Dec. 19

### Dec. 20

### Dec. 21

### Dec. 22

### Dec. 23

### Dec. 24

### Dec. 25

### Dec. 26

### Dec. 27

### Dec. 28

### Dec. 29

### Dec. 30

### Dec. 31



[lucille]: https://github.com/cozydev-pink/lucille
[sbt-typelevel]: https://github.com/typelevel/sbt-typelevel
[learn-smithy]: https://github.com/valencik/learn-smithy
[protosearch-binary]: https://github.com/cozydev-pink/protosearch/issues/156
[munit]: https://scalameta.org/munit/
[fixture-ordering]: https://github.com/scalameta/munit/pull/724
[drop-211]: https://github.com/scalameta/munit/pull/723
[protosearch-plugin]: https://github.com/cozydev-pink/protosearch/pull/148
[protosearch-stored]: https://github.com/cozydev-pink/protosearch/issues/154
[protosearch-stored-pr]: https://github.com/cozydev-pink/protosearch/pull/159
