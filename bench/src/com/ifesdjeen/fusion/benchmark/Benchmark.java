package com.ifesdjeen.fusion.benchmark;

import com.ifesdjeen.fusion.Fusion;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5)
@Measurement(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.SECONDS)
@Fork(value = 1)
@State(Scope.Thread)
public class Benchmark {

  @Param({"1000", "10000", "100000"})
  public int times;

  List<Integer> l;

  @Setup
  public void setup() {
    l = new ArrayList<>();

    for (int i = 0; i < times; i++) {
      l.add(i);
    }
  }

  @org.openjdk.jmh.annotations.Benchmark
  public void testFoldJavaStream() {
    l.stream()
     .filter((i) -> i % 2 == 0)
     .map((i) -> i + 1)
     .reduce(0, (acc, i) -> acc + i);
  }

  @org.openjdk.jmh.annotations.Benchmark
  public void testFoldFusion() {
//    Fusion.<Integer>from(l)
//      .filter((i) -> i % 2 == 0)
//      .map((i) -> i + 1)
//      .fold(0, (acc, i) -> acc + i);
  }

}
