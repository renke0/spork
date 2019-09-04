package spork.core.internal

import static spork.core.internal.ApplicationProperties.properties

import spock.lang.Unroll
import spork.test.SporkSpecification

@Unroll
class ApplicationPropertiesSpec extends SporkSpecification {
  def "properties()"() {
    when:
      def firstInstance = properties()
      def secondInstance = properties()
    then:
      firstInstance.is(secondInstance)
  }
}
