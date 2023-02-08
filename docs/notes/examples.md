Scala Examples
==============

I find learning by example super helpful.
I love when documentation has lots of examples I can copy/paste and tweak.

With that in mind, I'm going to collect some examples here that don't really fit into a large post or note.
Maybe they'll be useful.


## `Iterator#collect`

`collect` is such a handy method, but I always seem to forget about it.
If you'd like to filter and map a collection in a type safe manner, check out `collect`!

Consider a `List[(String, Option[Int])]`:

```scala mdoc:silent
val xs = List(("hi", Some(1)), ("bye", None))
```

You'd like to manipulate only the values where the `Option` is defined:

```scala mdoc
xs.collect{case (msg, Some(n)) => (msg, n*100)}
```


## `Stream#scanChunks`

A lot of stateful methods in fs2's `Stream` are implemented using `scanChunks` for efficiency.
It's similar to `scan` in that it takes a starting state, and function to compute the next state and values.
But it operates on chunks and maintains the chunk sizes.

Here we implement a function similar to `zipWithIndex` but the index is one the left and starts at 1.

```scala mdoc:silent
import fs2.{Pure, Stream}

def iZip[F[_], A](s: Stream[F, A]): Stream[F, (Int, A)] =
  s.scanChunks(1) { (index, c) =>
    var idx = index
    val out = c.map { o =>
      val r = (idx, o)
      idx += 1
      r
    }
    (idx, out) // new state and value
  }

val s: Stream[Pure, String] =
  Stream("hi", "hello", "bye!")
```

e.g.
```scala mdoc
  iZip(s).toList
```
