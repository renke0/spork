package spork.lang

import static spork.behavior.BehaviorProcessorProvider.behaviorProcessorProvider

import spock.lang.Specification

class Integration extends Specification {
  def cleanup() {
    behaviorProcessorProvider()
      .processors()
      *.reset()
  }
}
