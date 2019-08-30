package spork.internal

class ApplicationProperties extends PropertyResolver {
  private static singleton
  public static final String SPORK_CONFIGURATION_FILE = './spork.yml'

  ApplicationProperties() {
    super(SPORK_CONFIGURATION_FILE)
  }

  static properties() {
    if (!singleton) {
      singleton = new ApplicationProperties()
    }
    return singleton
  }
}
