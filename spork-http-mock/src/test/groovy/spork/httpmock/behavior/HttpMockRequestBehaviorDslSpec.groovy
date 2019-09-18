package spork.httpmock.behavior

import static spork.httpmock.value.BodyValue.JsonPathBodyValue
import static spork.httpmock.value.BodyValue.MatchingStrategy.LOOSE
import static spork.httpmock.value.BodyValue.MatchingStrategy.STRICT

import groovy.transform.TupleConstructor
import spock.lang.Subject
import spock.lang.Unroll
import spork.httpmock.value.BodyValue
import spork.httpmock.value.BodyValue.JsonBodyValue
import spork.httpmock.value.NamedParameter
import spork.httpmock.value.StringValue
import spork.httpmock.value.StringValue.PlainStringValue
import spork.httpmock.value.StringValue.RegexStringValue
import spork.test.SporkSpecification

@Unroll
class HttpMockRequestBehaviorDslSpec extends SporkSpecification {
  @Subject
  private requestBehavior = new HttpMockRequestBehaviorDsl()
  private firstParameter = { requestBehavior.request.queryParameters.first() }
  private firstHeader = { requestBehavior.request.headers.first() }

  def "to_path()"() {
    given:
      def path = '/path'
    when:
      requestBehavior.to_path(path)
    then:
      requestBehavior.request.path instanceof PlainStringValue
      (requestBehavior.request.path as PlainStringValue).plainValue == path
  }

  def "to_path_matching(regex)"() {
    given:
      def regex = '.*'
    when:
      requestBehavior.to_path_matching(regex)
    then:
      requestBehavior.request.path instanceof RegexStringValue
      (requestBehavior.request.path as RegexStringValue).regex.pattern() == regex
  }

  def "to_path_matching(pattern)"() {
    given:
      def pattern = ~/'.*'/
    when:
      requestBehavior.to_path_matching(pattern)
    then:
      requestBehavior.request.path instanceof RegexStringValue
      (requestBehavior.request.path as RegexStringValue).regex.pattern() == pattern.pattern()
  }

  def "to_path_matching(stringValue)"() {
    given:
      def stringValue = new StringValue() {}
    when:
      requestBehavior.to_path_matching(stringValue)
    then:
      requestBehavior.request.path.is(stringValue)
  }

  def "with_body_loosely_matching(#type)"() {
    when:
      requestBehavior.with_body_loosely_matching(body)
    then:
      requestBehavior.request.bodyValue instanceof JsonBodyValue
      json((requestBehavior.request.bodyValue as JsonBodyValue).json) == json(json)
      (requestBehavior.request.bodyValue as JsonBodyValue).strategy == LOOSE
    where:
      type     | body                                    | json
      'map'    | ['prop1': 'val1', 'prop2': 2]           | '{"prop1":"val1","prop2":2}'
      'object' | new TestObject(prop1: 'val1', prop2: 2) | '{"prop1":"val1","prop2":2}'
      'json'   | '{"prop1":"val1","prop2":2}'            | '{"prop1":"val1","prop2":2}'
  }

  def "with_body_strictly_matching(#type)"() {
    when:
      requestBehavior.with_body_strictly_matching(body)
    then:
      requestBehavior.request.bodyValue instanceof JsonBodyValue
      json((requestBehavior.request.bodyValue as JsonBodyValue).json) == json(json)
      (requestBehavior.request.bodyValue as JsonBodyValue).strategy == STRICT
    where:
      type     | body                                    | json
      'map'    | ['prop1': 'val1', 'prop2': 2]           | '{"prop1":"val1","prop2":2}'
      'object' | new TestObject(prop1: 'val1', prop2: 2) | '{"prop1":"val1","prop2":2}'
      'json'   | '{"prop1":"val1","prop2":2}'            | '{"prop1":"val1","prop2":2}'
  }

  def "with_body_matching_json_path()"() {
    given:
      def jsonPath = '$.hi'
    when:
      requestBehavior.with_body_matching_json_path(jsonPath)
    then:
      requestBehavior.request.bodyValue instanceof JsonPathBodyValue
      (requestBehavior.request.bodyValue as JsonPathBodyValue).jsonPath == jsonPath
  }

  def "with_body_matching()"() {
    given:
      def bodyValue = new BodyValue() {}
    when:
      requestBehavior.with_body_matching(bodyValue)
    then:
      requestBehavior.request.bodyValue.is(bodyValue)
  }

  def "with_query_parameter(string)"() {
    given:
      def name = 'name'
    when:
      requestBehavior.with_query_parameter(name)
    then:
      (firstParameter().key as PlainStringValue).plainValue == name
      (firstParameter().value as RegexStringValue).regex.pattern() == ".*"
  }

  def "with_query_parameter(stringValue)"() {
    given:
      def stringValue = new StringValue() {}
    when:
      requestBehavior.with_query_parameter(stringValue)
    then:
      firstParameter().key.is(stringValue)
      (firstParameter().value as RegexStringValue).regex.pattern() == ".*"
  }

  def "with_query_parameter(string, string)"() {
    given:
      def name = 'name'
      def value = 'value'
    when:
      requestBehavior.with_query_parameter(name, value)
    then:
      (firstParameter().key as PlainStringValue).plainValue == name
      (firstParameter().value as PlainStringValue).plainValue == value
  }

  def "with_query_parameter(string, stringValue)"() {
    given:
      def name = 'name'
      def value = new StringValue() {}
    when:
      requestBehavior.with_query_parameter(name, value)
    then:
      (firstParameter().key as PlainStringValue).plainValue == name
      firstParameter().value.is(value)
  }

  def "with_query_parameter(stringValue, string)"() {
    given:
      def name = new StringValue() {}
      def value = 'value'
    when:
      requestBehavior.with_query_parameter(name, value)
    then:
      firstParameter().key.is(name)
      (firstParameter().value as PlainStringValue).plainValue == value
  }

  def "with_query_parameter(stringValue, stringValue)"() {
    given:
      def name = new StringValue() {}
      def value = new StringValue() {}
    when:
      requestBehavior.with_query_parameter(name, value)
    then:
      firstParameter().key.is(name)
      firstParameter().value.is(value)
  }

  def "with_query_parameter_matching(string)"() {
    given:
      def nameRegex = '.*name'
    when:
      requestBehavior.with_query_parameter_matching(nameRegex)
    then:
      (firstParameter().key as RegexStringValue).regex.pattern() == nameRegex
      (firstParameter().value as RegexStringValue).regex.pattern() == ".*"
  }

  def "with_query_parameter_matching(pattern)"() {
    given:
      def nameRegex = ~/.*name/
    when:
      requestBehavior.with_query_parameter_matching(nameRegex)
    then:
      (firstParameter().key as RegexStringValue).regex.pattern() == nameRegex.pattern()
      (firstParameter().value as RegexStringValue).regex.pattern() == ".*"
  }

  def "with_query_parameter_matching(string, string)"() {
    given:
      def nameRegex = '.*name'
      def valueRegex = '.*value'
    when:
      requestBehavior.with_query_parameter_matching(nameRegex, valueRegex)
    then:
      (firstParameter().key as RegexStringValue).regex.pattern() == nameRegex
      (firstParameter().value as RegexStringValue).regex.pattern() == valueRegex
  }

  def "with_query_parameter_matching(pattern, pattern)"() {
    given:
      def nameRegex = ~/.*name/
      def valueRegex = ~/.*value/
    when:
      requestBehavior.with_query_parameter_matching(nameRegex, valueRegex)
    then:
      (firstParameter().key as RegexStringValue).regex.pattern() == nameRegex.pattern()
      (firstParameter().value as RegexStringValue).regex.pattern() == valueRegex.pattern()
  }

  def "with_query_parameter_matching(NamedParameter)"() {
    given:
      def name = new StringValue() {}
      def value = new StringValue() {}
      def parameter = new NamedParameter(name, value)
    when:
      requestBehavior.with_query_parameter_matching(parameter)
    then:
      firstParameter().key.is(name)
      firstParameter().value.is(value)
  }

  def "with_header(string)"() {
    given:
      def name = 'name'
    when:
      requestBehavior.with_header(name)
    then:
      (firstHeader().key as PlainStringValue).plainValue == name
      (firstHeader().value as RegexStringValue).regex.pattern() == ".*"
  }

  def "with_header(stringValue)"() {
    given:
      def stringValue = new StringValue() {}
    when:
      requestBehavior.with_header(stringValue)
    then:
      firstHeader().key.is(stringValue)
      (firstHeader().value as RegexStringValue).regex.pattern() == ".*"
  }

  def "with_header(string, string)"() {
    given:
      def name = 'name'
      def value = 'value'
    when:
      requestBehavior.with_header(name, value)
    then:
      (firstHeader().key as PlainStringValue).plainValue == name
      (firstHeader().value as PlainStringValue).plainValue == value
  }

  def "with_header(string, stringValue)"() {
    given:
      def name = 'name'
      def value = new StringValue() {}
    when:
      requestBehavior.with_header(name, value)
    then:
      (firstHeader().key as PlainStringValue).plainValue == name
      firstHeader().value.is(value)
  }

  def "with_header(stringValue, string)"() {
    given:
      def name = new StringValue() {}
      def value = 'value'
    when:
      requestBehavior.with_header(name, value)
    then:
      firstHeader().key.is(name)
      (firstHeader().value as PlainStringValue).plainValue == value
  }

  def "with_header(stringValue, stringValue)"() {
    given:
      def name = new StringValue() {}
      def value = new StringValue() {}
    when:
      requestBehavior.with_header(name, value)
    then:
      firstHeader().key.is(name)
      firstHeader().value.is(value)
  }

  def "with_header_matching(string)"() {
    given:
      def nameRegex = '.*name'
    when:
      requestBehavior.with_header_matching(nameRegex)
    then:
      (firstHeader().key as RegexStringValue).regex.pattern() == nameRegex
      (firstHeader().value as RegexStringValue).regex.pattern() == ".*"
  }

  def "with_header_matching(pattern)"() {
    given:
      def nameRegex = ~/.*name/
    when:
      requestBehavior.with_header_matching(nameRegex)
    then:
      (firstHeader().key as RegexStringValue).regex.pattern() == nameRegex.pattern()
      (firstHeader().value as RegexStringValue).regex.pattern() == ".*"
  }

  def "with_header_matching(string, string)"() {
    given:
      def nameRegex = '.*name'
      def valueRegex = '.*value'
    when:
      requestBehavior.with_header_matching(nameRegex, valueRegex)
    then:
      (firstHeader().key as RegexStringValue).regex.pattern() == nameRegex
      (firstHeader().value as RegexStringValue).regex.pattern() == valueRegex
  }

  def "with_header_matching(pattern, pattern)"() {
    given:
      def nameRegex = ~/.*name/
      def valueRegex = ~/.*value/
    when:
      requestBehavior.with_header_matching(nameRegex, valueRegex)
    then:
      (firstHeader().key as RegexStringValue).regex.pattern() == nameRegex.pattern()
      (firstHeader().value as RegexStringValue).regex.pattern() == valueRegex.pattern()
  }

  def "with_header_matching(NamedParameter)"() {
    given:
      def name = new StringValue() {}
      def value = new StringValue() {}
      def parameter = new NamedParameter(name, value)
    when:
      requestBehavior.with_header_matching(parameter)
    then:
      firstHeader().key.is(name)
      firstHeader().value.is(value)
  }

  @TupleConstructor
  private static class TestObject {
    String prop1
    int prop2
  }
}
