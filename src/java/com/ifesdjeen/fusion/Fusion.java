package com.ifesdjeen.fusion;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Fusion<INIT, FROM> {

  private final List<Function<Consumer, Consumer>> suppliers;

  public Fusion() {
    this(new ArrayList<>());
  }

  protected Fusion(List<Function<Consumer, Consumer>> suppliers) {
    this.suppliers = suppliers;
  }

  public <TO> Fusion<INIT, TO> map(Function<FROM, TO> fn) {
    suppliers.add((downstream) -> Operation.map(fn, downstream));
    return new Fusion<>(suppliers);
  }

  public Fusion<INIT, FROM> filter(Predicate<FROM> pred) {
    suppliers.add((downstream) -> Operation.filter(pred, downstream));
    return new Fusion<>(suppliers);
  }

  public Consumer<INIT> end(AtomicReference<FROM> init) {
    List<Function<Consumer, Consumer>> reversed = Lists.reverse(suppliers);
    Consumer stack = new Consumer<FROM>() {
      @Override
      public void accept(FROM o) {
        init.set(o);
      }
    };

    for (Function<Consumer, Consumer> gen : reversed) {
      stack = gen.apply(stack);
    }

    return stack;
  }

  public <ACC> Consumer<INIT> fold(AtomicReference<ACC> init,
                                   BiFunction<ACC, FROM, ACC> res) {
    List<Function<Consumer, Consumer>> reversed = Lists.reverse(suppliers);
    Consumer stack = new Consumer<FROM>() {
      @Override
      public void accept(FROM o) {
        init.updateAndGet((old) -> res.apply(old, o));
      }
    };

    for (Function<Consumer, Consumer> gen : reversed) {
      stack = gen.apply(stack);
    }

    return stack;
  }
}
