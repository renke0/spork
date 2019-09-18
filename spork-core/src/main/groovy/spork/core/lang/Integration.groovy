package spork.core.lang

import static spork.core.behavior.BehaviorAdapterProvider.behaviorAdapterProvider

import spock.lang.Specification

class Integration extends Specification {
  def cleanup() {
    behaviorAdapterProvider()
        .adapters()
        *.reset()
  }
}
