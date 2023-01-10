Vector Search
=============

The following is not meant to read like a blog post, it's really a collection of notes on something I've been reading about lately.
It's an attempt to help me think about these things and understand them better. It may or may not be useful to others.


## Intro

Lately I've been reading about vector search.
In this context, vector search is about finding a vector `v` that is "similar" to some query vector `q`, where both `q` and `v` represent some other underlying data.

A corpus of documents we want to search over may be lifted into an embedding space by some process, the same process is applied to the input query, and then a similarity function in the embedding space determines relevant results.

As an example, consider a database of metadata on books:

```scala mdoc:silent
case class Book(title: String, author: String)

val corpus: List[Book] = List(
  Book("The Tale of Peter Rabbit", "Beatrix Potter"),
  Book("The Tale of Two Bad Mice", "Beatrix Potter"),
  Book("One Fish, Two Fish, Red Fish, Blue Fish", "Dr. Suess"),
  Book("Green Eggs and Ham", "Dr. Suess"),
)
```

In classic keyword search, we might find results for a query by looking for books where the title contains words used in the query.

```scala mdoc
def tokens(input: String): Set[String] =
  input.toLowerCase().split(" ").toSet

def search(query: String): List[Book] =
  corpus.filter(b => 
    tokens(query).subsetOf(tokens(b.title))
  )

val twos = search("two")
```

Here our documents are lifted into a space of token sets by the `tokens` function.
Each book is represented by the set of lowercased words in its title.
We lift our input query into the same space by tokenizing it as well.
Our similarity function is simply the `subsetOf` function on sets.

In vector search, our corpus of documents may be lifted into an embedding space by some machine learning model, such that we have a numeric vector to represent each document.
Instead of `tokens` we might have `vector`:

```scala mdoc
def vector(input: String): Array[Float] = ???
```

Our similarity function will also have to change, perhaps to cosine similarity, perhaps to something else...


## Reads

### HNSW

[Lucene uses Hierarchical Navigable Small World (HNSW) graphs][lucene-hnsw] for its approximate nearest neighbor search.
I have not read or really skimmed [the original paper][hnsw-paper] yet.

HNSW appears to have issues scaling to high dimensions.
There is this [comparative study on HNSW][hnsw-comparative] that seems to express concerns over the claims from the original paper.
Lucene has a hard limit of 1024 dimensions and maintainers [do not seem keen on increasing this][lucene-dim-limit].
The performance problems of HNSW in Lucene appear to be such that an [alternative may be necessary][lucene-hnsw-alt].

> I'm really concerned about us locking ourselves into HNSW, and we must...must get away from it (its like 1000x slower than it should be).

It's worth noting that Elasticsearch performs quite poorly on the [approximate nearest neighbours benchmarks][ann-benchmarks] which is presumably using Lucene.


### ScaNN

[ScaNN] is a vector similarity search library from Google that introduces a new quantization technique specifically aimed at preserving the maximum inner-product search.

> Previous vector quantization schemes quantized database elements with the aim of minimizing the average distance between each vector x and its quantized form x̃.
> While this is a useful metric, optimizing for this is not equivalent to optimizing nearest-neighbor search accuracy.
> The key idea behind our paper is that encodings with higher average distance may actually result in superior MIPS accuracy.


[ann-benchmarks]: http://ann-benchmarks.com/
[ScaNN]: https://ai.googleblog.com/2020/07/announcing-scann-efficient-vector.html
[lucene-hnsw]: https://lucene.apache.org/core/9_4_2/core/org/apache/lucene/util/hnsw/HnswGraph.html
[hnsw-paper]: https://arxiv.org/abs/1603.09320
[hnsw-comparative]: https://arxiv.org/abs/1904.02077v1
[lucene-dim-limit]: https://github.com/apache/lucene/pull/874
[lucene-hnsw-alt]: https://github.com/apache/lucene/pull/11946#discussion_r1041043232
