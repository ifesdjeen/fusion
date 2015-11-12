package com.ifesdjeen.fusion.monoid;

import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

import java.util.Map;

public class PMapMonoid<K, T> implements Monoid<PMap<K, T>> {

  private final Monoid<T> keyMonoid;

  public PMapMonoid(Monoid<T> keyMonoid) {
    this.keyMonoid = keyMonoid;
  }

  @Override
  public PMap<K, T> mappend(PMap<K, T> left, PMap<K, T> right) {
    PMap<K, T> merged = left;
    for (Map.Entry<K, T> entry : right.entrySet()) {
      K key = entry.getKey();
      T value = entry.getValue();
      merged = merged.plus(key,
                           merged.containsKey(key) ?
                           keyMonoid.mappend(merged.get(key), value) :
                           keyMonoid.mappend(keyMonoid.mempty(), value));
    }
    return merged;
  }

  @Override
  public PMap<K, T> mempty() {
    return HashTreePMap.empty();
  }
}
