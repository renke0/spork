package spork.core.internal

import static spork.core.error.TestConfigurationException.checkConfiguration

import org.yaml.snakeyaml.Yaml
import spork.core.error.TestConfigurationException

class PropertyResolver {
  private Map properties

  PropertyResolver(String propertyFilePath) {
    initializeProperties(propertyFilePath)
  }

  String getAsString(String property, String defaultValue = null) {
    def raw = rawValue(property)
    return raw != null ? raw as String : defaultValue
  }

  BigDecimal getAsDecimal(String property, BigDecimal defaultValue = null) {
    def value = getAsString(property)
    try {
      return value ? new BigDecimal(value) : defaultValue
    } catch (NumberFormatException e) {
      throw new TestConfigurationException("Property $property is not a decimal", e)
    }
  }

  Integer getAsInteger(String property, Integer defaultValue = null) {
    def value = getAsString(property)
    try {
      return value ? Integer.valueOf(value) : defaultValue
    } catch (NumberFormatException e) {
      throw new TestConfigurationException("Property $property is not an integer", e)
    }
  }

  Boolean getAsBoolean(String property, Boolean defaultValue = null) {
    def value = getAsString(property)
    switch (value) {
      case 'true': return true
      case 'false': return false
      case null: return defaultValue
      default: throw new TestConfigurationException("Property $property is not a boolean")
    }
  }

  List getAsList(String property, List defaultValue = null) {
    def list = rawValue(property)
    checkConfiguration(!list || list instanceof List, "Property $property is not a list")
    return list ? list.collect { String.valueOf(it) } : defaultValue
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
