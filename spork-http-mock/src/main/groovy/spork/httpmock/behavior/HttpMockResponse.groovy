package spork.httpmock.behavior

import spork.http.HttpStatus

class HttpMockResponse {
  final Map<String, List<String>> headers = [:]
  final Map body = [:]
  HttpStatus status
}