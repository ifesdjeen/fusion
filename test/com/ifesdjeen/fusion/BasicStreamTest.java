package com.ifesdjeen.fusion;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class BasicStreamTest {

  @Test
  public void testOperation() {
    List<Integer> l = new ArrayList();
    for (int i = 0; i < 10000000; i++) {
      l.add(i);
    }


    for (int iter = 0; iter < 10; iter++) {
      {
        long beginning = System.currentTimeMillis();
        List<Integer> l2 = l.stream().filter((i) -> i % 2 == 0).map((i) -> i + 1).collect(Collectors.toList());
        System.out.println(l2.get(l2.size() - 1));
        System.out.println("Java Streams: " + (System.currentTimeMillis() - beginning));
      }

      {
        long beginning = System.currentTimeMillis();
        List<Integer> l2 = new ArrayList<>();
        Stream.fromList(l,
                        Operation.filter((Integer i) -> i % 2 == 0,
                                         Operation.map((i) -> i + 1,
                                                       Operation.toList(l2))));
        //        List<Integer> l2 = Operation.toList(Operation.map((i) -> i + 1,
        //                                                          Operation.filter((Integer i) -> i % 2 == 0,
        //                                                                           Stream.fromList(l))));
        System.out.println(l2.get(l2.size() - 1));
        System.out.println("Fusion: " + (System.currentTimeMillis() - beginning));
      }
    }

  }

  @Test
  public void testFold() {
    for (int iter = 0; iter < 20; iter++) {

      Random rand = new Random();
      List<Integer> l = new ArrayList();
      for (int i = 0; i < 10000000 + Math.abs(rand.nextInt(10)); i++) {
        l.add(i);
      }


      {
        long beginning = System.currentTimeMillis();
        Integer l2 = l.stream()
                      .filter((i) -> i % 2 == 0)
                      .map((i) -> i + 1)
                      .reduce(0, (acc, i) -> acc + i);
        System.out.println(l2);
        System.out.println("Java Streams: " + (System.currentTimeMillis() - beginning));
      }


      {
        long beginning = System.currentTimeMillis();

        AtomicReference<Integer> ref = new AtomicReference<>(0);
        Stream.fromList(l,
                        Operation.filter((Integer i) -> i % 2 == 0,
                                         Operation.map((i) -> i + 1,
                                                       Operation.fold(ref, (acc, i) -> acc + i))));
        System.out.println(ref.get());
        System.out.println("Fusion: " + (System.currentTimeMillis() - beginning));
      }
    }

  }

  @Test
  public void testSomething() {
    List<Integer> l = new ArrayList();
    for (int i = 0; i < 5; i++) {
      l.add(i);
    }

    {
      AtomicReference<Integer> ref = new AtomicReference<>(0);
      Stream.fromList(l,
                      Operation.filter((Integer i) -> i % 2 == 0,
                                       Operation.map((i) -> i + 1,
                                                     Operation.fold(ref, (acc, i) -> acc + i))));
      System.out.println(ref.get());
    }

  }

  @Test
  public void testFusion() {
    List<Integer> l = new ArrayList();
    for (int i = 0; i < 5; i++) {
      l.add(i);
    }

    {
      AtomicReference<Integer> ref1 = new AtomicReference<>(0);
      Stream.fromList(l,
                      new Fusion<Integer, Integer>().filter((i) -> i % 2 == 0)
                                                    .map((i) -> i + 1)
                                                    .fold(ref1,
                                                          (acc, i) -> acc + i));
      System.out.println(ref1.get());

      AtomicReference<Integer> ref = new AtomicReference<>(0);
      Stream.fromList(l,
                      Operation.filter((Integer i) -> i % 2 == 0,
                                       Operation.map((i) -> i + 1,
                                                     Operation.fold(ref, (acc, i) -> acc + i))));
      System.out.println(ref.get());
    }

  }
}
