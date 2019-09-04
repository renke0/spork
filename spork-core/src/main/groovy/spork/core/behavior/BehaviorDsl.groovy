package spork.core.behavior

import static BehaviorProcessorProvider.behaviorProcessorProvider
import static groovy.lang.Closure.DELEGATE_ONLY

abstract class BehaviorDsl {
  protected abstract Class<? extends BehaviorProcessor> processorType()

  protected setup(Behavior behavior) {
    def processor = behaviorProcessorProvider().getProcessorOfType(processorType())
    processor.setup(behavior)
    return behavior
  }

  protected static <T> T delegate(T delegator, Closure closure) {
    closure.delegate = delegator
    closure.resolveStrategy = DELEGATE_ONLY
    closure()
    return delegator
  }
}
