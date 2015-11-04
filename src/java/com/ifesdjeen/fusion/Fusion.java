package com.ifesdjeen.fusion;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Fusion<INIT, FROM> {

  private final List<Function<Consumer, Consumer>> suppliers;
  private final AtomicBoolean                      doBreak;

  public Fusion() {
    this(new ArrayList<>(),
         new AtomicBoolean());
  }

  protected Fusion(List<Function<Consumer, Consumer>> suppliers,
                   AtomicBoolean doBreak) {
    this.suppliers = suppliers;
    this.doBreak = doBreak;
  }

  public <TO> Fusion<INIT, TO> map(Function<FROM, TO> fn) {
    suppliers.add((downstream) -> Operation.map(fn, downstream));
    return downstream();
  }

  public Fusion<INIT, FROM> filter(Predicate<FROM> pred) {
    suppliers.add((downstream) -> Operation.filter(pred, downstream));
    return downstream();
  }

  public Fusion<INIT, FROM> take(int howMany) {
    suppliers.add((downstream) -> {
      AtomicInteger countdown = new AtomicInteger(howMany);
      return (i) -> {
        downstream.accept(i);
        int more = countdown.decrementAndGet();
        if (more == 0) {
          doBreak.set(true);
        }
      };

    });
    return downstream();
  }

  protected <TO> Fusion<INIT, TO> downstream() {
    return new Fusion<INIT, TO>(suppliers, doBreak);
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
                                   BiFunction<ACC, FROM, ACC> fold) {
    List<Function<Consumer, Consumer>> reversed = Lists.reverse(suppliers);
    Consumer stack = new Consumer<FROM>() {
      @Override
      public void accept(FROM o) {
        init.updateAndGet((old) -> fold.apply(old, o));
      }
    };

    for (Function<Consumer, Consumer> gen : reversed) {
      stack = gen.apply(stack);
    }

    return stack;
  }

  @SuppressWarnings("unchecked")
  public <ACC> ACC fold(ACC init,
                        BiFunction<ACC, FROM, ACC> fold,
                        Iterator<INIT> iterator) {
    List<Function<Consumer, Consumer>> reversed = Lists.reverse(suppliers);
    FusionFinaliser<FROM, ACC> finalizer = new FusionFinaliser<>(init,
                                                                 fold);
    Consumer stack = finalizer;
    for (Function<Consumer, Consumer> gen : reversed) {
      stack = gen.apply(stack);
    }

    Consumer<INIT> finalized = (Consumer<INIT>) stack;
    while (iterator.hasNext()) {
      if (doBreak.get()) {
        break;
      }
      finalized.accept(iterator.next());
    }
    return finalizer.get();
  }
}
