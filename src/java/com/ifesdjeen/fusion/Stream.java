package com.ifesdjeen.fusion;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public interface Stream<T> {

  public Step<T> next();

  public static <T> Stream<T> fromList(List<T> input) {
    return new Stream<T>() {

      private AtomicInteger pointer = new AtomicInteger(0);

      @Override
      public Step<T> next() {
        if (pointer.get() < input.size()) {
          return new Step.Yield(this, input.get(pointer.getAndIncrement()));
        }
        return new Step.Done<>();
      }

    };
  }


}
