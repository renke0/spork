package spork.httpmock

import spork.http.HttpMethod
import spork.httpmock.body.BodyMatcher

class HttpMockRequest {
  HttpMethod method
  String path
  final List<String> pathParameters = []
  final Map<String, List<String>> queryParameters = [:]
  final Map<String, List<String>> headers = [:]
  BodyMatcher bodyMatcher
}
