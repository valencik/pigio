Literal Types
=============

_The following was originally prepared for a talk at the [Ottawa Scala Enthusiasts meetup](https://web.archive.org/web/20211018003315/https://www.meetup.com/Ottawa-Scala-Enthusiasts/) back in November of 2019._


## Lockbox

We'll start off with a pretty silly example just to demonstrate using `Singleton` as an upper bound on a type parameter.
This tells the compiler to infer a singleton type at the call site.

```scala mdoc
case class Lockbox[T <: Singleton, A](key: T, message: A) {
  def unlock(k: T): A = message
  def backdoor(k: "martin"): A = message
}
```

Let's create a `Lockbox`:

```scala mdoc:silent
val box = Lockbox("password", "very secret msg")
```

If we unlock with the wrong password we get a type error:

```scala mdoc:fail
box.unlock("wrong")
```

Nice! The error message clearly shows the password it was expecting.
Hmmm, perhaps we shouldn't store anything too secret in here.

Let's check with the real key and the backdoor:

```scala mdoc
box.unlock("password")

box.backdoor("martin")
```

Works as expected.
To review, we can use the `Singleton` upper bound to infer a singleton type at call sites, and we can use literals like the string literal `"martin"` in type position.

## Frame

A perhaps more compelling use of literal types is to provide a nice interface for interacting with a data frame.

```scala mdoc
case class Frame[K1 <: Singleton, K2 <: Singleton, V1, V2](
    key1: K1,
    key2: K2)(
    rows: List[Tuple2[V1, V2]]
) {

  case class COL[K, V](
      val col: K => List[V]
  )
  object COL {
    implicit val K1V1: COL[K1, V1] = COL((k: K1) => rows.map(_._1))
    implicit val K2V2: COL[K2, V2] = COL((k: K2) => rows.map(_._2))
  }

  def col[A <: Singleton, B](k: A)(
      implicit columnKey: COL[A, B]
  ): List[B] =
    columnKey.col(k)
}
```

Let's make a frame:

```scala mdoc
val fruits = List(("apple", 2), ("orange", 1), ("banana", 3))
val f = Frame("name", "amount")(fruits)
```

And then grab and use some columns:

```scala mdoc
f.col("amount").sum

f.col("name").sorted.map(_.toUpperCase)
```

Notice that we don't need to specify the type of the column, inference works out here.

If we try and access a column thatd doesn't exist we get an implicit search error:

```scala mdoc:fail
f.col("oops")
```

## Frame With Dynamic

If we add the dynamic trait to the above `Frame` we can access columns as methods.

```scala mdoc
import scala.language.dynamics
case class DFrame[K1 <: Singleton, K2 <: Singleton, V1, V2](
    key1: K1,
    key2: K2)(
    rows: List[Tuple2[V1, V2]]
) extends Dynamic {

  case class COL[K, V](
      val col: K => List[V]
  )
  object COL {
    implicit val K1V1: COL[K1, V1] = COL((k: K1) => rows.map(_._1))
    implicit val K2V2: COL[K2, V2] = COL((k: K2) => rows.map(_._2))
  }

  def col[A <: Singleton, B](k: A)(
      implicit columnKey: COL[A, B]
  ): List[B] =
    columnKey.col(k)

  def selectDynamic[A <: Singleton, B](k: A)(implicit kv: COL[A, B]): List[B] =
    kv.col(k)
}
```
Let's make a fancy `DFrame`:

```scala mdoc
val df = DFrame("name", "amount")(fruits)
```

Accessing a column now looks like this:

```scala mdoc
df.name.filter(_.startsWith("b"))
```

With mistakes again failing to find an implicit:

```scala mdoc:fail
df.oops
```
