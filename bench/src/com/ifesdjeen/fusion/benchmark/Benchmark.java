package com.ifesdjeen.fusion.benchmark;

import com.ifesdjeen.fusion.Fusion;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.SECONDS)
@Fork(value = 1)
@State(Scope.Thread)
public class Benchmark {

  @Param({"1000", "10000", "100000", "10000000"})
  public int times;
  //
  //  @Test
  //  public void testOperation() {
  //    List<Integer> l = new ArrayList();
  //    for (int i = 0; i < 10000000; i++) {
  //      l.add(i);
  //    }
  //
  //
  //    for (int iter = 0; iter < 10; iter++) {
  //      {
  //        long beginning = System.currentTimeMillis();
  //        List<Integer> l2 = l.stream().filter((i) -> i % 2 == 0).map((i) -> i + 1).collect(Collectors.toList());
  //        System.out.println(l2.get(l2.size() - 1));
  //        System.out.println("Java Streams: " + (System.currentTimeMillis() - beginning));
  //      }
  //
  //      {
  //        long beginning = System.currentTimeMillis();
  //        List<Integer> l2 = new ArrayList<>();
  //        Stream.fromList(l,
  //                        Operation.filter((Integer i) -> i % 2 == 0,
  //                                         Operation.map((i) -> i + 1,
  //                                                       Operation.toList(l2))));
  //        System.out.println(l2.get(l2.size() - 1));
  //        System.out.println("Fusion: " + (System.currentTimeMillis() - beginning));
  //      }
  //    }
  //
  //  }

  List<Integer> l;
  Random        rand;

  @Setup
  public void setup() {
    l = new ArrayList<>();
    rand = new Random();

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
    Fusion.<Integer>from(l)
      .filter((i) -> i % 2 == 0)
      .map((i) -> i + 1)
      .fold(0, (acc, i) -> acc + i);
  }

  //  @Benchmark
  //  public void testFoldFusion() {
  //    AtomicReference<Integer> ref = new AtomicReference<>(0);
  //    Stream.fromList(l,
  //                    Operation.filter((Integer i) -> i % 2 == 0,
  //                                     Operation.map((i) -> i + 1,
  //                                                   Operation.fold(ref, (acc, i) -> acc + i))));
  //  }


  //  @Test
  //  public void testSomething() {
  //    List<Integer> l = new ArrayList();
  //    for (int i = 0; i < 5; i++) {
  //      l.add(i);
  //    }
  //
  //    {
  //      AtomicReference<Integer> ref = new AtomicReference<>(0);
  //      Stream.fromList(l,
  //                      Operation.filter((Integer i) -> i % 2 == 0,
  //                                       Operation.map((i) -> i + 1,
  //                                                     Operation.fold(ref, (acc, i) -> acc + i))));
  //      System.out.println(ref.get());
  //    }
  //
  //  }
  //
  //  @Test
  //  public void testFusion() {
  //    List<Integer> l = new ArrayList();
  //    for (int i = 0; i < 5; i++) {
  //      l.add(i);
  //    }
  //
  //    {
  //      AtomicReference<Integer> ref1 = new AtomicReference<>(0);
  //      Stream.fromList(l,
  //                      new Fusion<Integer, Integer>().filter((i) -> i % 2 == 0)
  //                                                    .map((i) -> i + 1)
  //                                                    .fold(ref1,
  //                                                          (acc, i) -> acc + i));
  //      System.out.println(ref1.get());
  //
  //      AtomicReference<Integer> ref = new AtomicReference<>(0);
  //      Stream.fromList(l,
  //                      Operation.filter((Integer i) -> i % 2 == 0,
  //                                       Operation.map((i) -> i + 1,
  //                                                     Operation.fold(ref, (acc, i) -> acc + i))));
  //      System.out.println(ref.get());
  //    }
  //  }
}
