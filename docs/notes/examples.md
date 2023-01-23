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
