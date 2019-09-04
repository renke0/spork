package spork.core.error

import spock.lang.Unroll
import spork.test.SporkSpecification

@Unroll
class TestConfigurationExceptionSpec extends SporkSpecification {
  def "checkConfiguration(true)"() {
    when:
      TestConfigurationException.checkConfiguration(true, 'message')
    then:
      noExceptionThrown()
  }

  def "checkConfiguration(false)"() {
    when:
      TestConfigurationException.checkConfiguration(false, 'message')
    then:
      def ex = thrown(TestConfigurationException)
      ex.message == 'message'
  }
}
