package spork.core.internal

import spock.lang.Subject
import spock.lang.Unroll
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
      def property = propertyResolver.getPropertyAsString('test.property.string')
    then:
      property instanceof String
      property == 'hi!'
  }

  def "getPropertyAsInteger()"() {
    when:
      def property = propertyResolver.getPropertyAsInteger('test.property.integer')
    then:
      property instanceof Integer
      property == 1
  }

  def "getPropertyAsDecimal()"() {
    when:
      def property = propertyResolver.getPropertyAsDecimal('test.property.decimal')
    then:
      property instanceof BigDecimal
      property == 1.5
  }

  def "getPropertyAsBoolean()"() {
    when:
      def property = propertyResolver.getPropertyAsBoolean('test.property.boolean')
    then:
      property instanceof Boolean
      !property
  }

  def "getPropertyAsList()"() {
    when:
      def property = propertyResolver.getPropertyAsList('test.property.list')
    then:
      property instanceof List
      property == ['item1', 'item2', 'item3']
  }
}
