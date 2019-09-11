package spork.core.internal

import static spork.core.error.TestConfigurationException.checkConfiguration

import org.yaml.snakeyaml.Yaml
import spork.core.error.TestConfigurationException

class PropertyResolver {
  private Map properties

  PropertyResolver(String propertyFilePath) {
    initializeProperties(propertyFilePath)
  }

  String getPropertyAsString(String property) {
    return rawValue(property) as String
  }

  BigDecimal getPropertyAsDecimal(String property) {
    def value = getPropertyAsString(property)
    try {
      return value ? new BigDecimal(value) : null
    } catch (NumberFormatException e) {
      throw new TestConfigurationException("Property $property is not a decimal", e)
    }
  }

  Integer getPropertyAsInteger(String property) {
    def value = getPropertyAsString(property)
    try {
      return value ? Integer.valueOf(value) : null
    } catch (NumberFormatException e) {
      throw new TestConfigurationException("Property $property is not an integer", e)
    }
  }

  Boolean getPropertyAsBoolean(String property) {
    def value = getPropertyAsString(property)
    switch (value) {
      case 'true': return true
      case 'false': return false
      case null: return null
      default: throw new TestConfigurationException("Property $property is not a boolean")
    }
  }

  List getPropertyAsList(String property) {
    def list = rawValue(property)
    checkConfiguration(!list || list instanceof List, "Property $property is not a list")
    return list ? list.collect { String.valueOf(it) } : null
  }

  private void initializeProperties(String propertyFilePath) {
    def url = getClass().getClassLoader().getResource(propertyFilePath)
    properties = url ? new Yaml().load(url.text) as Map : [:]
  }

  private rawValue(String property) {
    def chunks = property.split('\\.')
    def current = properties
    chunks.each { current = (current ?: [:])[it] }
    return current
  }
}
