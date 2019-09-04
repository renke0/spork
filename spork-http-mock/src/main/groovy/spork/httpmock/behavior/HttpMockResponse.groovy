package spork.httpmock.behavior

import spork.http.HttpStatus

class HttpMockResponse {
  HttpStatus status
  Map body
  final Map<String, List<String>> headers = [:]
}
