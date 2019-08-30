package spork.httpmock

import spork.behavior.BehaviorDsl
import spork.behavior.BehaviorProcessor
import spork.http.HttpStatus
import spork.httpmock.body.BodyMatcher

class HttpMockBehaviorDsl extends BehaviorDsl {

  def static httpMock(HttpMockRequestBehavior requestBehavior, HttpMockResponseBehavior responseBehavior) {
    def behavior = new HttpMockBehavior(requestBehavior.request, responseBehavior.response)
    new HttpMockBehaviorDsl().setup(behavior)
  }

  @Override
  protected Class<? extends BehaviorProcessor> processorType() {
    return HttpMockProcessor
  }

  def static any_http_request(@DelegatesTo(HttpMockRequestBehavior) Closure closure) {
    delegate(new HttpMockRequestBehavior(), closure)
  }

  def static will_return_a_response(@DelegatesTo(HttpMockResponseBehavior) Closure closure) {
    delegate(new HttpMockResponseBehavior(), closure)
  }

  private static paramList(def object) {
    if (object instanceof Collection)
      return object.collect { String.valueOf(it) }
    else
      return [String.valueOf(object as Object)]
  }

  static class HttpMockRequestBehavior {
    final HttpMockRequest request = new HttpMockRequest()

    void with_body_matching(BodyMatcher bodyMatcher) {
      request.bodyMatcher = bodyMatcher
    }

    void to_path(String path) {
      request.path = path
    }

    void with_query_parameters(Map<String, Object> parameters) {
      parameters.forEach { k, v -> with_query_parameter(k, v) }
    }

    void with_query_parameter(String name, def value) {
      request.queryParameters.put(name, paramList(value))
    }

    void with_headers(Map<String, Object> headers) {
      headers.forEach { k, v -> with_header(k, v) }
    }

    void with_header(String name, def value) {
      request.headers.put(name, paramList(value))
    }

    void with_path_parameters(def ... parameters) {
      request.pathParameters.addAll(parameters.collect { String.valueOf(it) })
    }
  }

  static class HttpMockResponseBehavior {
    final HttpMockResponse response = new HttpMockResponse()

    void with_status(HttpStatus status) {
      response.status = status
    }

    void with_body(Object body) {
      with_body([*: body.class.declaredFields
        .findAll { !it.synthetic }
        .collectEntries { [it.name, body."$it.name"] }])
    }

    void with_body(Map map) {
      response.body.clear()
      response.body << map
    }

    void with_headers(Map<String, Object> headers) {
      headers.forEach { k, v -> with_header(k, v) }
    }

    void with_header(String name, def value) {
      response.headers.put(name, paramList(value))
    }
  }
}
