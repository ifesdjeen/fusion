package com.ifesdjeen.fusion.monoid;

import org.junit.Test;
import org.pcollections.TreePVector;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PListMonoidTest {

  @Test
  public void monoidTest() {
    PListMonoid<Integer> monoid = new PListMonoid<>();
    assertThat(monoid.mappend(monoid.mempty(),
                              TreePVector.from(Arrays.asList(1, 2, 3))),
               is(TreePVector.from(Arrays.asList(1, 2, 3))));
  }

  @Test
  public void nonEmptyMonoidTest() {
    PListMonoid<Integer> monoid = new PListMonoid<>();
    assertThat(monoid.mappend(TreePVector.from(Arrays.asList(1, 2, 3)),
                              TreePVector.from(Arrays.asList(4, 5, 6))),
               is(TreePVector.from(Arrays.asList(1, 2, 3,
                                                 4, 5, 6))));
  }
}
