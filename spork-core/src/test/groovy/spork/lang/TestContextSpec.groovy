package spork.lang

import static spork.lang.ContextLevel.SCENARIO
import static spork.lang.ContextLevel.TEST_CASE
import static spork.lang.TestContext.testContext
import static spork.lang.Testing.randomString

import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class TestContextSpec extends Specification {

  def setup() {
    testContext().clearAll()
  }

  def "instance"() {
    when:
      testContext()
    then:
      innerContext().containsKey(SCENARIO)
      innerContext().containsKey(TEST_CASE)
  }

  def "save -> default"() {
    given:
      def subject = testContext()
      def key = randomString()
      def value = randomString()
    when:
      subject.save(key, value)
    then:
      innerContext().get(SCENARIO).get(key) == value
      innerContext().get(TEST_CASE).isEmpty()
  }

  def "save -> #level"() {
    given:
      def subject = testContext()
      def key = randomString()
      def value = randomString()
    when:
      subject.save(level as ContextLevel, key, value)
    then:
      innerContext().get(level).get(key) == value
    where:
      level << ContextLevel.values()
  }

  def "clear -> #level"() {
    given:
      def subject = testContext()
      subject.save(level as ContextLevel, randomString(), randomString())
    when:
      subject.clear(level as ContextLevel)
    then:
      innerContext().get(level).isEmpty()
    where:
      level << ContextLevel.values()
  }

  def "clearAll"() {
    given:
      def subject = testContext()
      ContextLevel.values()
        .each { subject.save(it, randomString(), randomString()) }
    when:
      subject.clearAll()
    then:
      innerContext().entrySet()
        .each { it.value.isEmpty() }
  }

  def "get -> simple access #level"() {
    given:
      def subject = testContext()
      def key = randomString()
      def value = randomString()
      subject.save(level as ContextLevel, key, value)
    when:
      def result = subject.get(key)
    then:
      result == value
    where:
      level << ContextLevel.values()
  }

  def "get -> overridden"() {
    given:
      def subject = testContext()
      def key = randomString()
      def value1 = randomString()
      def value2 = randomString()
    when:
      subject.save(SCENARIO as ContextLevel, key, value2)
      def result = subject.get(key)
      assert result == value2
      subject.save(TEST_CASE as ContextLevel, key, value1)
    then:
      result == value2
  }

  private static innerContext() {
    testContext().singleton.context
  }
}
