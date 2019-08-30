package spork.internal

import org.yaml.snakeyaml.Yaml

class PropertyResolver {
  private Map properties

  PropertyResolver(String propertyFilePath) {
    initializeProperties(propertyFilePath)
  }

  void initializeProperties(String propertyFilePath) {
    def url = getClass().getClassLoader().getResource(propertyFilePath)
    if (url) {
      properties = new Yaml().load(url.text)
    } else {
      properties = [:]
    }
  }

  String propertyAsString(String property) {
    return rawValue(property) as String
  }

  BigDecimal propertyAsDecimal(String property) {
    def value = propertyAsString(property)
    return value ? new BigDecimal(value) : null
  }

  Integer propertyAsInteger(String property) {
    def value = propertyAsString(property)
    return value ? Integer.valueOf(value) : null
  }

  boolean propertyAsBoolean(String property) {
    def value = propertyAsString(property)
    return value ? Boolean.valueOf(value) : null
  }

  List propertyAsList(String property) {
    def list = rawValue(property)
    return list ? list.collect { String.valueOf(it) } : null
  }

  private rawValue(String property) {
    def chunks = property.split('\\.')
    def current = properties
    chunks.each { current = (current ?: [:])[it] }
    return current
  }
}
