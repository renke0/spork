package spork.httpmock.behavior

import spork.httpmock.matcher.BodyMatcher
import spork.httpmock.matcher.NamedParameter
import spork.httpmock.matcher.StringMatcher

class HttpMockRequest {
  StringMatcher method
  StringMatcher path
  BodyMatcher bodyMatcher
  final List<NamedParameter> queryParameters = []
  final List<NamedParameter> headers = []
}
