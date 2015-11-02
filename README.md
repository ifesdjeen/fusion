# Fusion - Stream Fusion and complex aggregates for CassandraClusterConfig

This is an attempt to implement Stream Fusion API in Java. The main feature of the
fused stream is the ability to have an intermediate result on the each step of the
processing, which makes it very appealing for processing of the large amounts of data.

You can learn more about stream fusion by reading the paper on the Stream Fusion:
[From Streams to lists to nothing at all](http://citeseer.ist.psu.edu/viewdoc/download?doi=10.1.1.104.7401&rep=rep1&type=pdf).

Java implementation will certainly never be as elegant and concise as the Haskell
one, but we still decided to give it a try after seeing how well the Haskell version worked.

# What is stream fusion and why do I need it?

Stream fusion is a concept of combining several operations that are usually split
in chunks into a single-step op. For example, if you have a rather long list, and
you would like to first `filter` it, then `map` it and finally `fold` it to some
value, you would usually have to keep the elements from the previous step in order
to start with the next one.

Stream fusion combines `filter` `map` and `fold` into a single step operation
which is applied to every single item that is coming within the stream, therefore
after processing an item through an entire topology you don't have to store any
intermediate results at all. Someone has pointed out that the idea of stream fusion
is a lot like [Clojure transducers](http://clojure.org/transducers).

# License

Copyright(C) 2015-2016 Alex Petrov

Double licensed under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html) (the same as Clojure) or
the [Apache Public License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
