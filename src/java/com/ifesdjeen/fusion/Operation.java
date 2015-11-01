package com.ifesdjeen.fusion;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Operation<T, D> extends Stream<D> {

  //  protected final Stream<T> parent;
  //
  //  public Operation(Stream<T> stream) {
  //    this.parent = stream;
  //  }


  public static <FROM, TO> Operation<FROM, TO> map(Function<FROM, TO> fn, Stream<FROM> stream) {
    return () -> {
      Step step = stream.next();
      if (step.getState() == State.YIELD) {
        step.setItem(fn.apply(step.getItem()));
      }
      return step;
    };

  }

  public static <T> Operation<T, T> filter(Predicate<T> pred, Stream<T> stream) {
    return () -> {
      Step step = stream.next();
      if (step.getState() == State.YIELD && pred.test(step.getItem())) {
        return new Step(State.SKIP, null);
      }
      return step;
    };
  }

  public static <ACC, T> ACC fold(Stream<T> stream,
                                  ACC init_,
                                  BiFunction<ACC, T, ACC> foldf) {
    ACC init = init_;

    Step s = stream.next();

    while (true) {
      if (s.getState() == State.YIELD) {
        init = foldf.apply(init, s.getItem());
      } else if (s.getState() == State.DONE) {
        break;
      }

      s = stream.next();
    }

    return init;
  }

  public static <T> List<T> toList(Stream<T> stream) {
    List<T> list = new ArrayList<>();

    Step s = stream.next();

    while (s.getState() != State.DONE) {
      if (s.getState() == State.YIELD) {
        list.add(s.getItem());
      }
      s = stream.next();
    }

    return list;
  }


}
