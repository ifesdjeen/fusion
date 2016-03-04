package com.ifesdjeen.fusion.monoid;


import org.pcollections.PVector;
import org.pcollections.TreePVector;

public class PListMonoid<T> implements Monoid<PVector<T>> {

  @Override
  public PVector<T> mappend(PVector<T> left, PVector<T> right) {
    return left.plusAll(right);
  }

  @Override
  public PVector<T> mempty() {
    return TreePVector.empty();
  }


}
