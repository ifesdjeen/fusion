# Fusion - Stream Fusion and complex aggregates for CassandraClusterConfig

This is an attempt to implement Stream Fusion API in Java. The main feature of the
fused stream is the ability to have an intermediate result on the each step of the
processing, which makes it very appealing for processing of the large amounts of data.

You can learn more about stream fusion by reading the paper on the Stream Fusion:
[From Streams to lists to nothing at all](http://citeseer.ist.psu.edu/viewdoc/download?doi=10.1.1.104.7401&rep=rep1&type=pdf).

Java implementation will certainly never be as elegant and concise as the Haskell
one, but we still decided to give it a try after seeing how well the Haskell version worked.

# License

Copyright(C) 2015-2016 Alex Petrov

Double licensed under the [Eclipse Public License](http://www.eclipse.org/legal/epl-v10.html) (the same as Clojure) or
the [Apache Public License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
