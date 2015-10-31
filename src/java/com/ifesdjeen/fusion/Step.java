package com.ifesdjeen.fusion;

public class Step<T> {


  public static class Done<T> extends Step<T> {

  }

  public static class Skip<T> extends Step<T> {

    private final Stream<T> stream;

    public Skip(Stream<T> stream) {
      this.stream = stream;
    }

    public Stream<T> getStream() {
      return stream;
    }
  }

  public static class Yield<T> extends Step<T> {

    private final T         item;
    private final Stream<T> stream;

    public Yield(Stream<T> stream, T item) {
      this.stream = stream;
      this.item = item;
    }

    public T getItem() {
      return item;
    }

    public Stream<T> getStream() {
      return stream;
    }
  }
}
