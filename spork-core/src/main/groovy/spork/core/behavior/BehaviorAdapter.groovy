package spork.core.behavior

interface BehaviorAdapter<T extends Behavior> {
  Object setup(T behavior)

  void reset()
}
