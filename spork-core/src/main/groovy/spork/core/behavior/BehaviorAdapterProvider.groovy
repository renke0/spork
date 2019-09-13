package spork.core.behavior

import static spork.core.error.TestConfigurationException.checkConfiguration

import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.ConfigurationBuilder

class BehaviorAdapterProvider {
  private static BehaviorAdapterProvider singleton

  final Map<Class, BehaviorAdapter> adapters

  private BehaviorAdapterProvider() {
    adapters = initialize()
  }

  static BehaviorAdapterProvider behaviorAdapterProvider() {
    if (!singleton)
      singleton = new BehaviorAdapterProvider()
    return singleton
  }

  def <T extends BehaviorAdapter> T getAdapterOfType(Class<T> behaviorAdapterInterface) {
    checkConfiguration(adapters.containsKey(behaviorAdapterInterface),
      "There is no adapter for behavior ${behaviorAdapterInterface.name}")
    return adapters.get(behaviorAdapterInterface) as T
  }

  List<BehaviorAdapter> adapters() {
    return List.copyOf(adapters.values())
  }

  private static Map<Class, BehaviorAdapter> initialize() {
    def scanner = new Reflections(new ConfigurationBuilder()
      .addUrls(forJavaClassPath())
      .setScanners(new SubTypesScanner()))

    def allAdapters = scanner.getSubTypesOf(BehaviorAdapter)
    def interfaces = allAdapters.findAll { it.isInterface() && it.interfaces.contains(BehaviorAdapter) }
    return interfaces.collectEntries { [(it): findAdapter(it, allAdapters).getConstructor().newInstance()] }
  }

  private static findAdapter(Class adapterInterface, Set<Class> allAdapters) {
    def adapters = allAdapters.findAll { it.interfaces.contains(adapterInterface) }
    checkConfiguration(!adapters.empty, "There are no adapters found for ${adapterInterface.name}")
    checkConfiguration(adapters.size() == 1, "There are multiple adapters found for ${adapterInterface.name}")
    return adapters.first()
  }

  static Collection<URL> forJavaClassPath() {
    def javaClassPath = System.getProperty("java.class.path")
    return javaClassPath.split(File.pathSeparator)
      .collect { new File(it).toURI().toURL() }
      .unique { it.toExternalForm() }
  }
}
