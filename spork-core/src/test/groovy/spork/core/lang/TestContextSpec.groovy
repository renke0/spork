package spork.core.lang

import static spork.core.lang.ContextLevel.SCENARIO
import static spork.core.lang.ContextLevel.TEST_CASE
import static spork.core.lang.TestContext.testContext

import spock.lang.Subject
import spock.lang.Unroll
import spork.test.SporkSpecification

@Unroll
class TestContextSpec extends SporkSpecification {

  @Subject
  private testContext = testContext()

  def setup() {
    testContext().clearAll()
  }

  def "testContext()"() {
    when:
      def firstInstance = testContext()
      def secondInstance = testContext()
    then:
      firstInstance.is(secondInstance)
      innerContext().containsKey(SCENARIO)
      innerContext().containsKey(TEST_CASE)
  }

  def "save() -> saving to default context"() {
    given:
      def key = randomString()
      def value = randomString()
    when:
      testContext.save(key, value)
    then:
      innerContext().get(SCENARIO).get(key) == value
      innerContext().get(TEST_CASE).isEmpty()
  }

  def "save(#level)"() {
    given:
      def key = randomString()
      def value = randomString()
    when:
      testContext.save(level as ContextLevel, key, value)
    then:
      innerContext().get(level).get(key) == value
    where:
      level << ContextLevel.values()
  }

  def "clear(#level)"() {
    given:
      testContext.save(level as ContextLevel, randomString(), randomString())
    when:
      testContext.clear(level as ContextLevel)
    then:
      innerContext().get(level).isEmpty()
    where:
      level << ContextLevel.values()
  }

  def "clearAll()"() {
    given:
      ContextLevel.values()
          .each { testContext.save(it, randomString(), randomString()) }
    when:
      testContext.clearAll()
    then:
      innerContext().entrySet()
          .each { it.value.isEmpty() }
  }

  def "get() -> getting from #level context"() {
    given:
      def key = randomString()
      def value = randomString()
      testContext.save(level as ContextLevel, key, value)
    when:
      def result = testContext.get(key)
    then:
      result == value
    where:
      level << ContextLevel.values()
  }

  def "get() -> getting an overridden property"() {
    given:
      def key = randomString()
      def value1 = randomString()
      def value2 = randomString()
    when:
      testContext.save(SCENARIO as ContextLevel, key, value2)
      def result = testContext.get(key)
      assert result == value2
      testContext.save(TEST_CASE as ContextLevel, key, value1)
    then:
      result == value2
  }

  def "get() -> getting a non existing property"() {
    given:
      def key = randomString()
    when:
      def result = testContext.get(key)
    then:
      !result
  }

  private static innerContext() {
    testContext().singleton.context
  }
}
