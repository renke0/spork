package spork.mockserver

import spork.httpmock.behavior.HttpMockBehavior
import spork.httpmock.behavior.HttpMockProcessor

class HttpMockMockserverProcessor implements HttpMockProcessor {
  @Override
  Object setup(HttpMockBehavior behaviorDefinition) {
    return null
  }
}
