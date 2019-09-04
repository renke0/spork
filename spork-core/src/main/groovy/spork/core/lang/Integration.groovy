package spork.core.lang

import static spork.core.behavior.BehaviorProcessorProvider.behaviorProcessorProvider

import spock.lang.Specification

class Integration extends Specification {
  def cleanup() {
    behaviorProcessorProvider()
      .processors()
      *.reset()
  }
}
