package spork.core

import spork.core.behavior.Behavior
import spork.core.behavior.BehaviorDsl
import spork.core.behavior.BehaviorProcessor

class TestBehavior implements Behavior {}

interface TestBehaviorProcessor extends BehaviorProcessor<TestBehavior> {}

class TestBehaviorProcessorImplementation implements TestBehaviorProcessor {
  @Override
  Object setup(TestBehavior behavior) {
    return null
  }

  @Override
  void reset() {}
}

class TestBehaviorDsl extends BehaviorDsl {
  @Override
  protected Class<? extends BehaviorProcessor> processorType() {
    return TestBehaviorProcessorImplementation
  }
}
