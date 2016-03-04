package com.ifesdjeen.fusion.monoid;

import org.junit.Test;
import org.pcollections.HashPMap;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PMapMonoidTest {

  @Test
  public void pemptyTest() {
    Monoid<Integer> sumMonoid = new SumMonoid();
    Monoid<PMap<String, Integer>> mapMonoid = new PMapMonoid<>(sumMonoid);

    assertThat(mapMonoid.mappend(mapMonoid.mempty(),
                                 HashTreePMap
                                   .<String, Integer>empty()
                                   .plus("key1", 1)),
               is(HashTreePMap
                    .<String, Integer>empty()
                    .plus("key1", 1))
              );
  }

  @Test
  public void nonEmptyMonoidTest() {
    Monoid<Integer> sumMonoid = new SumMonoid();
    Monoid<PMap<String, Integer>> mapMonoid = new PMapMonoid<>(sumMonoid);

    assertThat(mapMonoid.mappend(HashTreePMap
                                   .<String, Integer>empty()
                                   .plus("key1", 1),
                                 HashTreePMap
                                   .<String, Integer>empty()
                                   .plus("key1", 5)),
               is(HashTreePMap
                    .<String, Integer>empty()
                    .plus("key1", 6))
              );
  }



}