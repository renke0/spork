package spork.lang

import static spork.lang.ContextLevel.SCENARIO
import static spork.lang.TestContext.testContext

import spock.lang.Specification

class Integration extends Specification {
  def cleanup() {
    testContext().clear(SCENARIO)
  }
}
