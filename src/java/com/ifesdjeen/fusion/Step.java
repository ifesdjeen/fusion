package com.ifesdjeen.fusion;

public class Step {

  private State state;
  private Object item;

  public Step(State state, Object item) {
    this.state = state;
    this.item = item;
  }

  public State getState() {
    return state;
  }

  public <T> T getItem() {
    return (T)item;
  }

  public void setState(State state) {
    this.state = state;
  }

  public void setItem(Object item) {
    this.item = item;
  }
}
