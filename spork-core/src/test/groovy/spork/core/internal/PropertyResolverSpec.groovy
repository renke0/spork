package spork.core.internal

import spock.lang.Subject
import spock.lang.Unroll
import spork.core.error.TestConfigurationException
import spork.test.SporkSpecification

@Unroll
class PropertyResolverSpec extends SporkSpecification {
  private static final String PROPERTY_FILE = './test-properties.yml'

  @Subject
  private propertyResolver = new PropertyResolver(PROPERTY_FILE)

  def "PropertyResolver()"() {
    when:
      def instance = new PropertyResolver(PROPERTY_FILE)
    then:
      !instance.properties.isEmpty()
  }

  def "getPropertyAsString()"() {
    when:
      def value = propertyResolver.getPropertyAsString(property)
    then:
      value == expectation
    where:
      label               | property                             | expectation
      'existing'          | 'test.property.string'               | 'hi!'
      'non-existing'      | 'test.property.non-existing'         | null
      'non-existing-root' | 'non-existing.property.non-existing' | null
  }

  def "getPropertyAsInteger() -> #label"() {
    when:
      def value = propertyResolver.getPropertyAsInteger(property)
    then:
      value == expectation
    where:
      label          | property                     | expectation
      'existing'     | 'test.property.integer'      | 1
      'non-existing' | 'test.property.non-existing' | null
  }

  def "getPropertyAsInteger() -> non integer"() {
    when:
      propertyResolver.getPropertyAsInteger('test.property.string')
    then:
      thrown(TestConfigurationException)
  }

  def "getPropertyAsDecimal() -> #label"() {
    when:
      def value = propertyResolver.getPropertyAsDecimal(property)
    then:
      value == expectation
    where:
      label          | property                     | expectation
      'existing'     | 'test.property.decimal'      | 1.5
      'non-existing' | 'test.property.non-existing' | null
  }

  def "getPropertyAsDecimal() -> non decimal"() {
    when:
      propertyResolver.getPropertyAsDecimal('test.property.string')
    then:
      thrown(TestConfigurationException)
  }

  def "getPropertyAsBoolean() -> #label"() {
    when:
      def value = propertyResolver.getPropertyAsBoolean(property)
    then:
      value == expectation
    where:
      label            | property                      | expectation
      'existing true'  | 'test.property.boolean-true'  | true
      'existing false' | 'test.property.boolean-false' | false
      'non-existing'   | 'test.property.non-existing'  | null
  }

  def "getPropertyAsBoolean() -> non boolean"() {
    when:
      propertyResolver.getPropertyAsBoolean('test.property.integer')
    then:
      thrown(TestConfigurationException)
  }

  def "getPropertyAsList() -> #label"() {
    when:
      def value = propertyResolver.getPropertyAsList(property)
    then:
      value == expectation
    where:
      label          | property                     | expectation
      'existing'     | 'test.property.list'         | ['item1', 'item2', 'item3']
      'non-existing' | 'test.property.non-existing' | null
  }

  def "getPropertyAsList() -> non list"() {
    when:
      propertyResolver.getPropertyAsList('test.property.string')
    then:
      thrown(TestConfigurationException)
  }
}
