package spork.httmock

import spork.behavior.Behavior
import spork.http.HttpMethod
import spork.http.HttpStatus

class HttpMock extends Behavior {
  final List<HttpMockDefinition> mocks = []

  def static mock(@DelegatesTo(HttpMock) Closure closure) {
    delegate(new HttpMock(), closure)
  }

  void any_http_request_to(HttpMethod method, String path, @DelegatesTo(HttpMockDefinition) Closure closure) {
    mocks << delegate(new HttpMockDefinition(method, path), closure)
  }

  class HttpMockDefinition {
    private boolean finishedRequest
    final HttpMethod requestMethod
    final String requestPath
    final List<String> pathParameters = []
    final Map<String, List<String>> requestParameters = [:]
    final Map<String, List<String>> requestHeaders = [:]
    final Map<String, List<String>> responseHeaders = [:]
    final Map responseBody = [:]
    final Map requestBody = [:]
    HttpStatus responseStatus

    private HttpMockDefinition(HttpMethod requestMethod, String requestPath) {
      this.requestMethod = requestMethod
      this.requestPath = requestPath
    }

    void with_body(Map body) {
      currentBody() << body
    }

    void with_request_parameter(String name, def value) {
      requestParameters.put(name, paramList(value))
    }

    void with_request_parameters(Map<String, Object> parameters) {
      parameters.forEach { k, v -> with_request_parameter(k, v) }
    }

    void with_path_parameters(def ... parameters) {
      pathParameters.addAll(parameters.collect { String.valueOf(it) })
    }

    void with_header(String name, def value) {
      currentHeaders().put(name, paramList(value))
    }

    void with_headers(Map<String, Object> headers) {
      headers.forEach { k, v -> with_header(k, v) }
    }

    void will_return_a_response_with_status(HttpStatus status) {
      responseStatus = status
      finishedRequest = true
    }

    private currentBody() {
      finishedRequest ? responseBody : requestBody
    }

    private currentHeaders() {
      finishedRequest ? responseHeaders : requestHeaders
    }

    private paramList(def object) {
      if (object instanceof Collection)
        return object.collect { String.valueOf(it) }
      else
        return [String.valueOf(object as Object)]
    }
  }
}
