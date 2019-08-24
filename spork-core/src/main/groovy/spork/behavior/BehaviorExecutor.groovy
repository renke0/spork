package spork.behavior

interface BehaviorExecutor<T extends Behavior> {
  void execute(T behavior)
}
