package spork.core.behavior

import static org.reflections.util.ClasspathHelper.forJavaClassPath
import static spork.core.error.TestConfigurationException.checkConfiguration

import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.ConfigurationBuilder

class BehaviorProcessorProvider {
  private static BehaviorProcessorProvider singleton

  final Map<Class, BehaviorProcessor> processors

  private BehaviorProcessorProvider() {
    processors = initialize()
  }

  static BehaviorProcessorProvider behaviorProcessorProvider() {
    if (!singleton)
      singleton = new BehaviorProcessorProvider()
    return singleton
  }

  def <T extends BehaviorProcessor> T getProcessorOfType(Class<T> behaviorProcessorInterface) {
    checkConfiguration(processors.containsKey(behaviorProcessorInterface),
      "There is no processor for behavior ${behaviorProcessorInterface.name}")
    return processors.get(behaviorProcessorInterface) as T
  }

  List<BehaviorProcessor> processors() {
    return List.copyOf(processors.values())
  }

  private static Map<Class, BehaviorProcessor> initialize() {
    def scanner = new Reflections(new ConfigurationBuilder()
      .addUrls(forJavaClassPath())
      .setScanners(new SubTypesScanner()))

    def allProcessors = scanner.getSubTypesOf(BehaviorProcessor)
    def interfaces = allProcessors.findAll { it.isInterface() && it.interfaces.contains(BehaviorProcessor) }
    return interfaces.collectEntries { [(it): findProcessor(it, allProcessors).getConstructor().newInstance()] }
  }

  private static findProcessor(Class processorInterface, Set<Class> allProcessors) {
    def processors = allProcessors.findAll { it.interfaces.contains(processorInterface) }
    checkConfiguration(!processors.empty, "There are no processors found for ${processorInterface.name}")
    checkConfiguration(processors.size() == 1, "There are multiple processors found for ${processorInterface.name}")
    return processors.first()
  }
}
