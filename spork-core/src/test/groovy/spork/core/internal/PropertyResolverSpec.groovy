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

  def "getAsString()"() {
    when:
      def value = propertyResolver.getAsString(property)
    then:
      value == expectation
    where:
      label               | property                             | expectation
      'existing'          | 'test.property.string'               | 'hi!'
      'non-existing'      | 'test.property.non-existing'         | null
      'non-existing-root' | 'non-existing.property.non-existing' | null
  }

  def "getAsInteger() -> #label"() {
    when:
      def value = propertyResolver.getAsInteger(property)
    then:
      value == expectation
    where:
      label          | property                     | expectation
      'existing'     | 'test.property.integer'      | 1
      'non-existing' | 'test.property.non-existing' | null
  }

  def "getAsInteger() -> non integer"() {
    when:
      propertyResolver.getAsInteger('test.property.string')
    then:
      thrown(TestConfigurationException)
  }

  def "getAsDecimal() -> #label"() {
    when:
      def value = propertyResolver.getAsDecimal(property)
    then:
      value == expectation
    where:
      label          | property                     | expectation
      'existing'     | 'test.property.decimal'      | 1.5
      'non-existing' | 'test.property.non-existing' | null
  }

  def "getAsDecimal() -> non decimal"() {
    when:
      propertyResolver.getAsDecimal('test.property.string')
    then:
      thrown(TestConfigurationException)
  }

  def "getAsBoolean() -> #label"() {
    when:
      def value = propertyResolver.getAsBoolean(property)
    then:
      value == expectation
    where:
      label            | property                      | expectation
      'existing true'  | 'test.property.boolean-true'  | true
      'existing false' | 'test.property.boolean-false' | false
      'non-existing'   | 'test.property.non-existing'  | null
  }

  def "getAsBoolean() -> non boolean"() {
    when:
      propertyResolver.getAsBoolean('test.property.integer')
    then:
      thrown(TestConfigurationException)
  }

  def "getAsList() -> #label"() {
    when:
      def value = propertyResolver.getAsList(property)
    then:
      value == expectation
    where:
      label          | property                     | expectation
      'existing'     | 'test.property.list'         | ['item1', 'item2', 'item3']
      'non-existing' | 'test.property.non-existing' | null
  }

  def "getAsList() -> non list"() {
    when:
      propertyResolver.getAsList('test.property.string')
    then:
      thrown(TestConfigurationException)
  }
}
