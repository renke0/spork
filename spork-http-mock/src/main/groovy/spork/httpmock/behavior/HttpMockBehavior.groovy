package spork.httpmock.behavior

import spork.core.behavior.Behavior

class HttpMockBehavior implements Behavior {
  final HttpMockRequest request
  final HttpMockResponse response

  HttpMockBehavior(HttpMockRequest request, HttpMockResponse response) {
    this.request = request
    this.response = response
  }
}
