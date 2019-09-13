package spork.core

import spork.core.behavior.Behavior
import spork.core.behavior.BehaviorDsl
import spork.core.behavior.BehaviorAdapter

class TestBehavior implements Behavior {}

interface TestBehaviorAdapter extends BehaviorAdapter<TestBehavior> {}

class TestBehaviorAdapterImplementation implements TestBehaviorAdapter {
  @Override
  Object setup(TestBehavior behavior) {
    return null
  }

  @Override
  void reset() {}
}

class TestBehaviorDsl extends BehaviorDsl {
  @Override
  protected Class<? extends BehaviorAdapter> adapterType() {
    return TestBehaviorAdapterImplementation
  }
}
