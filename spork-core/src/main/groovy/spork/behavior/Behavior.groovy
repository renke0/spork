package spork.behavior

import static groovy.lang.Closure.DELEGATE_ONLY

import spork.error.TestConfigurationException

abstract class Behavior {
  protected static <T> T delegate(T delegator, Closure closure) {
    closure.delegate = delegator
    closure.resolveStrategy = DELEGATE_ONLY
    closure()
    return delegator
  }

  protected static require(def required, String name) {
    if (required == null)
      throw new TestConfigurationException("$name is required to be set before setting new parameters")
  }
}
