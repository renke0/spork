package spork.lang

import spock.lang.Specification
import spork.internal.PropertyResolver

class Integration extends Specification {
  def setupSpec() {
    PropertyResolver.properties()
    TestContext.testContext()
  }
}
