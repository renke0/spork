package spork.httpmock.behavior

import spork.httpmock.value.BodyValue
import spork.httpmock.value.NamedParameter
import spork.httpmock.value.StringValue

class HttpMockRequest {
  StringValue method
  StringValue path
  BodyValue bodyValue
  final List<NamedParameter> queryParameters = []
  final List<NamedParameter> headers = []
}
