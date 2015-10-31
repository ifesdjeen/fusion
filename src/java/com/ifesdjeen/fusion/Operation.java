package com.ifesdjeen.fusion;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Operation<T, D> implements Stream<D> {

  protected final Stream<T> parent;

  public Operation(Stream<T> stream) {
    this.parent = stream;
  }



  protected abstract Step<D> step(Step.Done<T> done);

  protected abstract Step<D> step(Step.Skip<T> skip);

  protected abstract Step<D> step(Step.Yield<T> yield);

  public Step<D> next() {
    Step<T> prev = parent.next();
    if (prev instanceof Step.Done) {
      return step((Step.Done<T>) prev);
    } else if (prev instanceof Step.Yield) {
      return step((Step.Yield<T>) prev);
    } else {
      return step((Step.Skip<T>) prev);
    }
  }

  public static <FROM, TO> Operation<FROM, TO> map(Function<FROM, TO> fn, Stream<FROM> stream) {
    return new Operation<FROM, TO>(stream) {
      @Override
      public Step<TO> step(Step.Done<FROM> done) {
        return new Step.Done<>();
      }

      @Override
      protected Step<TO> step(Step.Skip<FROM> skip) {
        return new Step.Skip<>(this);
      }

      @Override
      public Step<TO> step(Step.Yield<FROM> yield) {
        return new Step.Yield<>(this, fn.apply(yield.getItem()));
      }
    };
  }

  public static <T> Operation<T, T> filter(Predicate<T> pred, Stream<T> stream) {
    return new Operation<T, T>(stream) {

      @Override
      protected Step<T> step(Step.Done<T> done) {
        return done;
      }

      @Override
      protected Step<T> step(Step.Skip<T> skip) {
        return skip;
      }

      @Override
      protected Step<T> step(Step.Yield<T> yield) {
        T item = yield.getItem();
        if (pred.test(item)) {
          return new Step.Yield<>(this, item);
        } else {
          return new Step.Skip<>(this);
        }

      }
    };
  }

  public static <T> List<T> toList(Stream<T> stream) {
    List<T> list = new ArrayList<>();

    Step<T> s = stream.next();

    while(!(s instanceof Step.Done)) {
      if (s instanceof Step.Yield) {
        list.add(((Step.Yield<T>) s).getItem());
      }
      s = stream.next();
    }

    return list;
  }
}
