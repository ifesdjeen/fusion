package com.ifesdjeen.fusion;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class BasicStreamTest {

  @Test
  public void testOperation() {
    List<Integer> l = Arrays.asList(1, 2, 3, 4, 5, 6);
    List<Integer> l2 = Operation.toList(Operation.filter((Integer i) -> i % 2 == 0,
                                                         Operation.map((i) -> i + 1,
                                                                       Stream.fromList(l))));

    System.out.println(l2);

  }
}
