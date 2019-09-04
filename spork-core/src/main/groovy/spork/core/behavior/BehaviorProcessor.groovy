package spork.core.behavior

interface BehaviorProcessor<T extends Behavior> {
  Object setup(T behavior)

  void reset()
}
