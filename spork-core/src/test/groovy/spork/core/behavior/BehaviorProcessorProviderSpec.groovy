package spork.core.behavior

import static spork.core.behavior.BehaviorProcessorProvider.behaviorProcessorProvider

import spock.lang.Unroll
import spork.core.TestBehaviorProcessor
import spork.core.TestBehaviorProcessorImplementation
import spork.test.SporkSpecification

@Unroll
class BehaviorProcessorProviderSpec extends SporkSpecification {
  def "behaviorProcessorProvider()"() {
    when:
      def firstInstance = behaviorProcessorProvider()
      def secondInstance = behaviorProcessorProvider()
    then:
      firstInstance.is(secondInstance)
  }

  def "getProcessorOfType()"() {
    when:
      def processor = behaviorProcessorProvider().getProcessorOfType(TestBehaviorProcessor)
    then:
      processor.class == TestBehaviorProcessorImplementation
  }

  def "processors()"() {
    when:
      def processors = behaviorProcessorProvider().processors()
    then:
      processors.size() == 1
      processors.get(0).class == TestBehaviorProcessorImplementation
  }
}
