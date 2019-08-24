package spork.internal

import org.yaml.snakeyaml.Yaml

class PropertyResolver {
  private static PropertyResolver singleton
  private Map properties

  static PropertyResolver properties() {
    if (!singleton) {
      singleton = new PropertyResolver()
    }
    return singleton
  }

  private PropertyResolver() {
    initializeProperties()
  }

  void initializeProperties() {
    def url = getClass().getResource("/spork.yml")
    if (!url) {
      url = getClass().getResource("/spork.yaml")
    }
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

  private rawValue(String property) {
    def chunks = property.split('\\.')
    def current = properties
    chunks.each { current = (current ?: [:])[it] }
    return current
  }
}
