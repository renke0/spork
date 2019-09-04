package spork.core.error

import groovy.transform.InheritConstructors

@InheritConstructors
class TestConfigurationException extends RuntimeException {
  static void checkConfiguration(boolean condition, String message) {
    if (!condition)
      throw new TestConfigurationException(message)
  }
}
