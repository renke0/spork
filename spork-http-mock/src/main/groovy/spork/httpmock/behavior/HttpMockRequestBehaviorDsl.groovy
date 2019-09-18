package spork.httpmock.behavior

import java.util.regex.Pattern
import spork.httpmock.value.BodyValue
import spork.httpmock.value.NamedParameter
import spork.httpmock.value.StringValue

class HttpMockRequestBehaviorDsl {
  final HttpMockRequest request = new HttpMockRequest()

  void to_any_path() {
    to_path_matching(StringValue.anyValue())
  }

  void to_path(String path) {
    to_path_matching(StringValue.plainText(path))
  }

  void to_any_path_except(String path) {
    to_path_matching(StringValue.plainText(path).not())
  }

  void to_path_matching(String regex) {
    to_path_matching(StringValue.matchingRegex(regex))
  }

  void to_path_matching(Pattern pattern) {
    to_path_matching(StringValue.matchingRegex(pattern))
  }

  void to_path_matching(StringValue stringValue) {
    request.path = stringValue
  }

  void to_path_not_matching(String regex) {
    to_path_matching(StringValue.matchingRegex(regex).not())
  }

  void to_path_not_matching(Pattern regex) {
    to_path_matching(StringValue.matchingRegex(regex).not())
  }

  void to_path_not_matching(StringValue stringValue) {
    to_path_matching(stringValue.not())
  }

  void with_body_loosely_matching(Map body) {
    with_body_matching(BodyValue.loosely(body))
  }

  void with_body_loosely_matching(String body) {
    with_body_matching(BodyValue.loosely(body))
  }

  void with_body_loosely_matching(Object body) {
    with_body_matching(BodyValue.loosely(body))
  }

  void with_body_strictly_matching(Map body) {
    with_body_matching(BodyValue.strictly(body))
  }

  void with_body_strictly_matching(String body) {
    with_body_matching(BodyValue.strictly(body))
  }

  void with_body_strictly_matching(Object body) {
    with_body_matching(BodyValue.strictly(body))
  }

  void with_body_matching_json_path(String jsonPath) {
    with_body_matching(new BodyValue.JsonPathBodyValue(jsonPath))
  }

  void with_body_matching(BodyValue bodyValue) {
    request.bodyValue = bodyValue
  }

  void with_query_parameter(String name) {
    with_query_parameter_matching(new NamedParameter(StringValue.plainText(name), StringValue.anyValue()))
  }

  void with_query_parameter(StringValue name) {
    with_query_parameter_matching(new NamedParameter(name, StringValue.anyValue()))
  }

  void with_query_parameter(String name, String value) {
    with_query_parameter_matching(new NamedParameter(StringValue.plainText(name), StringValue.plainText(value)))
  }

  void with_query_parameter(StringValue name, String value) {
    with_query_parameter_matching(new NamedParameter(name, StringValue.plainText(value)))
  }

  void with_query_parameter(String name, StringValue value) {
    with_query_parameter_matching(new NamedParameter(StringValue.plainText(name), value))
  }

  void with_query_parameter(StringValue name, StringValue value) {
    with_query_parameter_matching(new NamedParameter(name, value))
  }

  void with_query_parameter_matching(String nameRegex) {
    with_query_parameter_matching(new NamedParameter(StringValue.matchingRegex(nameRegex), StringValue.anyValue()))
  }

  void with_query_parameter_matching(Pattern nameRegex) {
    with_query_parameter_matching(new NamedParameter(StringValue.matchingRegex(nameRegex), StringValue.anyValue()))
  }

  void with_query_parameter_matching(String nameRegex, String valueRegex) {
    with_query_parameter_matching(new NamedParameter(StringValue.matchingRegex(nameRegex), StringValue.matchingRegex(valueRegex)))
  }

  void with_query_parameter_matching(Pattern nameRegex, Pattern valueRegex) {
    with_query_parameter_matching(new NamedParameter(StringValue.matchingRegex(nameRegex), StringValue.matchingRegex(valueRegex)))
  }

  void with_query_parameter_matching(NamedParameter queryParameter) {
    request.queryParameters << queryParameter
  }

  void with_header(String name) {
    with_header_matching(new NamedParameter(StringValue.plainText(name), StringValue.anyValue()))
  }

  void with_header(StringValue name) {
    with_header_matching(new NamedParameter(name, StringValue.anyValue()))
  }

  void with_header(String name, String value) {
    with_header_matching(new NamedParameter(StringValue.plainText(name), StringValue.plainText(value)))
  }

  void with_header(StringValue name, String value) {
    with_header_matching(new NamedParameter(name, StringValue.plainText(value)))
  }

  void with_header(String name, StringValue value) {
    with_header_matching(new NamedParameter(StringValue.plainText(name), value))
  }

  void with_header(StringValue name, StringValue value) {
    with_header_matching(new NamedParameter(name, value))
  }

  void with_header_matching(String nameRegex) {
    with_header_matching(new NamedParameter(StringValue.matchingRegex(nameRegex), StringValue.anyValue()))
  }

  void with_header_matching(Pattern nameRegex) {
    with_header_matching(new NamedParameter(StringValue.matchingRegex(nameRegex), StringValue.anyValue()))
  }

  void with_header_matching(String nameRegex, String valueRegex) {
    with_header_matching(new NamedParameter(StringValue.matchingRegex(nameRegex), StringValue.matchingRegex(valueRegex)))
  }

  void with_header_matching(Pattern nameRegex, Pattern valueRegex) {
    with_header_matching(new NamedParameter(StringValue.matchingRegex(nameRegex), StringValue.matchingRegex(valueRegex)))
  }

  void with_header_matching(NamedParameter header) {
    request.headers << header
  }
}
