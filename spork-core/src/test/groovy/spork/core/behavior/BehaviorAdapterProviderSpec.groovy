package spork.core.behavior

import static BehaviorAdapterProvider.behaviorAdapterProvider

import spock.lang.Unroll
import spork.core.TestBehaviorAdapter
import spork.core.TestBehaviorAdapterImplementation
import spork.test.SporkSpecification

@Unroll
class BehaviorAdapterProviderSpec extends SporkSpecification {
  def "behaviorAdapterProvider()"() {
    when:
      def firstInstance = behaviorAdapterProvider()
      def secondInstance = behaviorAdapterProvider()
    then:
      firstInstance.is(secondInstance)
  }

  def "getAdapterOfType()"() {
    when:
      def adapter = behaviorAdapterProvider().getAdapterOfType(TestBehaviorAdapter)
    then:
      adapter.class == TestBehaviorAdapterImplementation
  }

  def "adapters()"() {
    when:
      def adapters = behaviorAdapterProvider().adapters()
    then:
      adapters.size() == 1
      adapters.get(0).class == TestBehaviorAdapterImplementation
  }
}
