package spork.mockserver

import spork.httpmock.HttpMockBehavior
import spork.httpmock.HttpMockProcessor

class HttpMockMockserverProcessor implements HttpMockProcessor {
  @Override
  Object setup(HttpMockBehavior behaviorDefinition) {
    return null
  }
}
