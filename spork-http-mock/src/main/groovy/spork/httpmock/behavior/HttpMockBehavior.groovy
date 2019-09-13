package spork.httpmock.behavior

import static spork.core.error.TestConfigurationException.checkConfiguration

import spork.core.behavior.Behavior

class HttpMockBehavior implements Behavior {
  final HttpMockRequest request
  final HttpMockResponse response

  HttpMockBehavior(HttpMockRequest request, HttpMockResponse response) {
    this.request = request
    this.response = valid(response)
  }

  static def valid(HttpMockResponse response) {
    checkConfiguration(response.status != null, "The mocked response does not have a status")
    return response
  }
}
