package spork.behavior

interface BehaviorProcessor<T extends Behavior> {
  Object setup(T behaviorDefinition)

  void reset()
}
