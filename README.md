# Faster Java Streams

Stream fusion is a concept of combining multiple operations into the single step.
For example, if you have a rather long list, and you would like to first `filter`
it, then `map` it and finally `fold` it to some value, you would usually have to
keep the elements from the previous step in order to start with the next one.

`Fusion` combines `filter` `map` and `fold` into a single step operation
which is applied to every single item that is coming within the stream, therefore
after processing an item through an entire topology you don't have to store any
intermediate results at all.

Someone has pointed out that the idea of stream fusion is a bit like
[Clojure transducers](http://clojure.org/transducers).

`Fusion` is also introducing a concept of `Either`, which helps to combine
operations that may fail without explicit exception handling on each step.

# How does it work?

The simple example is to fold the list, by `filtering` only the even items, then
`mapping` the filtered items by incrementing them by one and then `folding` them
together into the sum:

```java
Integer res = new Fusion<Integer, Integer>()
    .filter((i) -> i % 2 == 0)
    .map((i) -> i + 1)
    .fold(0,
          (acc, i) -> acc + i,
          new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).iterator());
```

The way the operations are going to be combined looks something like that:
  * as a top-level consumer, you will have a `filter` wrapper
  * `filter` will call the `map` if the predicate returns true
  * `map` will trigger a call to `fold`

All these three items will be "fused" into the single consumer, which would
be an equivalent of the following code:

```java
AtomicInteger result = 0;
Consumer<Integer> consumer = (Integer i) -> {
  if (i % 2 == 0) {                // filter
    int iPrime = i + 1;            // map

    result.updateAndGet((old) -> { //fold
      return old + iPrime;
    });
  }
};

// Iteration
Iterator<Integer> inter = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).iterator();
while (iter.hasNext()) {
  consumer.accept(iter.next());
}

Integer final = result.get();      // Final result
```

# Cooperative parallelism

# Thread affinity

# Continuations

# Fusion - Stream Fusion and complex aggregates

This is an attempt to implement Stream Fusion API in Java. The main feature of the
fused stream is the ability to have an intermediate result on the each step of the
processing, which makes it very appealing for processing of the large amounts of data.

You can learn more about stream fusion by reading the paper on the Stream Fusion:
[From Streams to lists to nothing at all](http://citeseer.ist.psu.edu/viewdoc/download?doi=10.1.1.104.7401&rep=rep1&type=pdf).

Java implementation will certainly never be as elegant and concise as the Haskell
one, but we still decided to give it a try after seeing how well the Haskell version worked.

# Primitive streams

# Map Streams

# JMH Benchmarks

One of the main goals for the project is to maintain a minimal memory footprint,
avoid batching and operate on the primary existing data structures.

```
Benchmark                     (times)   Mode  Cnt       Score      Error  Units
Benchmark.testFoldFusion         1000  thrpt    5  121032.957 ± 8196.370  ops/s
Benchmark.testFoldFusion        10000  thrpt    5   11895.739 ± 1403.678  ops/s
Benchmark.testFoldFusion       100000  thrpt    5    1000.866 ±  209.908  ops/s
Benchmark.testFoldJavaStream     1000  thrpt    5  123787.341 ± 2618.969  ops/s
Benchmark.testFoldJavaStream    10000  thrpt    5   12657.628 ±  819.217  ops/s
Benchmark.testFoldJavaStream   100000  thrpt    5    1323.071 ±  179.718  ops/s
```

# License

Copyright(C) 2015-2016 Alex Petrov

Double licensed under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html) (the same as Clojure) or
the [Apache Public License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
