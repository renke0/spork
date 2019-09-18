package spork.httpmock.behavior

import groovy.json.JsonOutput
import spork.http.HttpStatus

class HttpMockResponseBehaviorDsl {
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
    with_body(JsonOutput.toJson(map))
  }

  void with_body(String body) {
    response.body = body
  }

  void with_header(String name, def value) {
    response.headers.put(name, paramList(value))
  }

  private static paramList(def object) {
    if (object instanceof Collection)
      return object.collect { String.valueOf(it) }
    else
      return [String.valueOf(object as Object)]
  }
}
