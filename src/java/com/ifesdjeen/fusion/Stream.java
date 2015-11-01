package com.ifesdjeen.fusion;

import java.util.List;

public interface Stream<T> {

  public Step next();

  public static <T> Stream<T> fromList(List<T> input) {
    return new Stream<T>() {

      private int pointer = 0;

      private Step step = new Step(State.YIELD, null);
      @Override
      public Step next() {
        if (pointer < input.size()) {
          step.setItem(input.get(pointer++));
          step.setState(State.YIELD);
        } else {
          step.setItem(null);
          step.setState(State.DONE);
        }
        return step;
      }

    };
  }


}
