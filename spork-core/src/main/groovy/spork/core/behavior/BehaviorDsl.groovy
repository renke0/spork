package spork.core.behavior

import static BehaviorAdapterProvider.behaviorAdapterProvider
import static groovy.lang.Closure.DELEGATE_ONLY

abstract class BehaviorDsl {
  protected abstract Class<? extends BehaviorAdapter> adapterType()

  protected setup(Behavior behavior) {
    def adapter = behaviorAdapterProvider().getAdapterOfType(adapterType())
    adapter.setup(behavior)
    return behavior
  }

  protected static <T> T delegate(T delegator, Closure closure) {
    closure.delegate = delegator
    closure.resolveStrategy = DELEGATE_ONLY
    closure()
    return delegator
  }
}
