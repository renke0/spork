package spork.core.lang

import spork.core.behavior.BehaviorAdapter
import spork.core.behavior.BehaviorAdapterProvider
import spork.test.SporkSpecification

class IntegrationSpec extends SporkSpecification {
  def "cleanup() -> reset adapters"() {
    given:
      BehaviorAdapterProvider behaviorAdapterProviderMock = Mock()
      BehaviorAdapter adapter1 = Mock()
      BehaviorAdapter adapter2 = Mock()

      GroovyMock(BehaviorAdapterProvider, global: true)
      BehaviorAdapterProvider.behaviorAdapterProvider() >> behaviorAdapterProviderMock
      behaviorAdapterProviderMock.adapters() >> [adapter1, adapter2]
    when:
      new Integration().cleanup()
    then:
      1 * adapter1.reset()
      1 * adapter2.reset()
  }
}
