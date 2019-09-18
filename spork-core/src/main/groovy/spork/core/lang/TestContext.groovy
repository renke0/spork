package spork.core.lang

import static java.util.Map.ofEntries
import static spork.core.lang.ContextLevel.SCENARIO

import java.util.Map.Entry

class TestContext {
  private static TestContext singleton

  private final Map<TestContext, Map<String, Object>> context

  static testContext() {
    if (!singleton) {
      singleton = new TestContext()
    }
    return singleton
  }

  private TestContext() {
    context = ofEntries(
        ContextLevel.values().collect { new MapEntry(it, [:]) } as Entry[])
  }

  void save(ContextLevel level = SCENARIO, String key, def value) {
    context.get(level)
        .put(key, value)
  }

  void clear(ContextLevel level) {
    context.get(level)
        .clear()
  }

  void clearAll() {
    ContextLevel.values()
        .each { clear(it) }
  }

  def get(String key) {
    return ContextLevel.values()
        .collect { context.get(it) }
        .find { it.containsKey(key) }
        ?.get(key)
  }
}
