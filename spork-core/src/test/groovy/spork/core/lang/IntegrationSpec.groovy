package spork.core.lang

import spork.core.behavior.BehaviorProcessor
import spork.core.behavior.BehaviorProcessorProvider
import spork.test.SporkSpecification

class IntegrationSpec extends SporkSpecification {
  def "cleanup() -> reset processors"() {
    given:
      BehaviorProcessorProvider behaviorProcessorProviderMock = Mock()
      BehaviorProcessor processor1 = Mock()
      BehaviorProcessor processor2 = Mock()

      GroovyMock(BehaviorProcessorProvider, global: true)
      BehaviorProcessorProvider.behaviorProcessorProvider() >> behaviorProcessorProviderMock
      behaviorProcessorProviderMock.processors() >> [processor1, processor2]
    when:
      new Integration().cleanup()
    then:
      1 * processor1.reset()
      1 * processor2.reset()
  }
}
