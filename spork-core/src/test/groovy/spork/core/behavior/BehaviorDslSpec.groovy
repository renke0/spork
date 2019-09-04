package spork.core.behavior

import static groovy.lang.Closure.DELEGATE_ONLY

import spork.core.TestBehavior
import spork.core.TestBehaviorDsl
import spork.test.SporkSpecification

class BehaviorDslSpec extends SporkSpecification {
  def "setup()"() {
    given:
      def behavior = new TestBehavior()
      BehaviorProcessorProvider behaviorProcessorProviderMock = Mock()
      BehaviorProcessor behaviorProcessorMock = Mock()

      GroovyMock(BehaviorProcessorProvider, global: true)
      BehaviorProcessorProvider.behaviorProcessorProvider() >> behaviorProcessorProviderMock
      behaviorProcessorProviderMock.getProcessorOfType(_ as Class) >> behaviorProcessorMock

      def dsl = new TestBehaviorDsl()
    when:
      def result = dsl.setup(behavior)
    then:
      1 * behaviorProcessorMock.setup(_ as Behavior)
      behavior.is(result)
  }

  def "delegate()"() {
    given:
      def any = new Object()
      def closure = { -> }
    when:
      def delegate = BehaviorDsl.delegate(any, closure)
    then:
      delegate.is(any)
      closure.delegate.is(any)
      closure.resolveStrategy == DELEGATE_ONLY
  }
}
