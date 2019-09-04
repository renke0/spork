package spork.core.internal

import org.yaml.snakeyaml.Yaml

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
    return value ? new BigDecimal(value) : null
  }

  Integer getPropertyAsInteger(String property) {
    def value = getPropertyAsString(property)
    return value ? Integer.valueOf(value) : null
  }

  boolean getPropertyAsBoolean(String property) {
    def value = getPropertyAsString(property)
    return value ? Boolean.valueOf(value) : null
  }

  List getPropertyAsList(String property) {
    def list = rawValue(property)
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
