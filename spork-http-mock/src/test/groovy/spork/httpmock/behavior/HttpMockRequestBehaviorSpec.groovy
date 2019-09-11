package spork.httpmock.behavior

import static spork.httpmock.matcher.BodyMatcher.JsonPathBodyMatcher
import static spork.httpmock.matcher.BodyMatcher.MatchingStrategy.LOOSE
import static spork.httpmock.matcher.BodyMatcher.MatchingStrategy.STRICT

import groovy.transform.TupleConstructor
import spock.lang.Subject
import spock.lang.Unroll
import spork.httpmock.behavior.HttpMockBehaviorDsl.HttpMockRequestBehavior
import spork.httpmock.matcher.BodyMatcher
import spork.httpmock.matcher.BodyMatcher.JsonBodyMatcher
import spork.httpmock.matcher.NamedParameter
import spork.httpmock.matcher.StringMatcher
import spork.httpmock.matcher.StringMatcher.PlainStringMatcher
import spork.httpmock.matcher.StringMatcher.RegexStringMatcher
import spork.test.SporkSpecification

@Unroll
class HttpMockRequestBehaviorSpec extends SporkSpecification {
  @Subject
  private requestBehavior = new HttpMockRequestBehavior()
  private firstParameter = { requestBehavior.request.queryParameters.first() }
  private firstHeader = { requestBehavior.request.headers.first() }

  def "with_body_loosely_matching(#type)"() {
    when:
      requestBehavior.with_body_loosely_matching(body)
    then:
      requestBehavior.request.bodyMatcher instanceof JsonBodyMatcher
      json((requestBehavior.request.bodyMatcher as JsonBodyMatcher).json) == json(json)
      (requestBehavior.request.bodyMatcher as JsonBodyMatcher).strategy == LOOSE
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
      requestBehavior.request.bodyMatcher instanceof JsonBodyMatcher
      json((requestBehavior.request.bodyMatcher as JsonBodyMatcher).json) == json(json)
      (requestBehavior.request.bodyMatcher as JsonBodyMatcher).strategy == STRICT
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
      requestBehavior.request.bodyMatcher instanceof JsonPathBodyMatcher
      (requestBehavior.request.bodyMatcher as JsonPathBodyMatcher).jsonPath == jsonPath
  }

  def "with_body_matching()"() {
    given:
      def bodyMatcher = new BodyMatcher() {}
    when:
      requestBehavior.with_body_matching(bodyMatcher)
    then:
      requestBehavior.request.bodyMatcher.is(bodyMatcher)
  }

  def "to_path()"() {
    given:
      def path = '/path'
    when:
      requestBehavior.to_path(path)
    then:
      requestBehavior.request.path instanceof PlainStringMatcher
      (requestBehavior.request.path as PlainStringMatcher).plainValue == path
  }

  def "to_path_matching(regex)"() {
    given:
      def regex = '.*'
    when:
      requestBehavior.to_path_matching(regex)
    then:
      requestBehavior.request.path instanceof RegexStringMatcher
      (requestBehavior.request.path as RegexStringMatcher).regex.pattern() == regex
  }

  def "to_path_matching(pattern)"() {
    given:
      def pattern = ~/'.*'/
    when:
      requestBehavior.to_path_matching(pattern)
    then:
      requestBehavior.request.path instanceof RegexStringMatcher
      (requestBehavior.request.path as RegexStringMatcher).regex.pattern() == pattern.pattern()
  }

  def "to_path_matching(matcher)"() {
    given:
      def matcher = new StringMatcher() {}
    when:
      requestBehavior.to_path_matching(matcher)
    then:
      requestBehavior.request.path.is(matcher)
  }

  def "with_query_parameter(string)"() {
    given:
      def name = 'name'
    when:
      requestBehavior.with_query_parameter(name)
    then:
      (firstParameter().nameMatcher as PlainStringMatcher).plainValue == name
      (firstParameter().valueMatcher as RegexStringMatcher).regex.pattern() == ".*"
  }

  def "with_query_parameter(matcher)"() {
    given:
      def matcher = new StringMatcher() {}
    when:
      requestBehavior.with_query_parameter(matcher)
    then:
      firstParameter().nameMatcher.is(matcher)
      (firstParameter().valueMatcher as RegexStringMatcher).regex.pattern() == ".*"
  }

  def "with_query_parameter(string, string)"() {
    given:
      def name = 'name'
      def value = 'value'
    when:
      requestBehavior.with_query_parameter(name, value)
    then:
      (firstParameter().nameMatcher as PlainStringMatcher).plainValue == name
      (firstParameter().valueMatcher as PlainStringMatcher).plainValue == value
  }

  def "with_query_parameter(string, matcher)"() {
    given:
      def name = 'name'
      def value = new StringMatcher() {}
    when:
      requestBehavior.with_query_parameter(name, value)
    then:
      (firstParameter().nameMatcher as PlainStringMatcher).plainValue == name
      firstParameter().valueMatcher.is(value)
  }

  def "with_query_parameter(matcher, string)"() {
    given:
      def name = new StringMatcher() {}
      def value = 'value'
    when:
      requestBehavior.with_query_parameter(name, value)
    then:
      firstParameter().nameMatcher.is(name)
      (firstParameter().valueMatcher as PlainStringMatcher).plainValue == value
  }

  def "with_query_parameter(matcher, matcher)"() {
    given:
      def name = new StringMatcher() {}
      def value = new StringMatcher() {}
    when:
      requestBehavior.with_query_parameter(name, value)
    then:
      firstParameter().nameMatcher.is(name)
      firstParameter().valueMatcher.is(value)
  }

  def "with_query_parameter_matching(string)"() {
    given:
      def nameRegex = '.*name'
    when:
      requestBehavior.with_query_parameter_matching(nameRegex)
    then:
      (firstParameter().nameMatcher as RegexStringMatcher).regex.pattern() == nameRegex
      (firstParameter().valueMatcher as RegexStringMatcher).regex.pattern() == ".*"
  }

  def "with_query_parameter_matching(pattern)"() {
    given:
      def nameRegex = ~/.*name/
    when:
      requestBehavior.with_query_parameter_matching(nameRegex)
    then:
      (firstParameter().nameMatcher as RegexStringMatcher).regex.pattern() == nameRegex.pattern()
      (firstParameter().valueMatcher as RegexStringMatcher).regex.pattern() == ".*"
  }

  def "with_query_parameter_matching(string, string)"() {
    given:
      def nameRegex = '.*name'
      def valueRegex = '.*value'
    when:
      requestBehavior.with_query_parameter_matching(nameRegex, valueRegex)
    then:
      (firstParameter().nameMatcher as RegexStringMatcher).regex.pattern() == nameRegex
      (firstParameter().valueMatcher as RegexStringMatcher).regex.pattern() == valueRegex
  }

  def "with_query_parameter_matching(pattern, pattern)"() {
    given:
      def nameRegex = ~/.*name/
      def valueRegex = ~/.*value/
    when:
      requestBehavior.with_query_parameter_matching(nameRegex, valueRegex)
    then:
      (firstParameter().nameMatcher as RegexStringMatcher).regex.pattern() == nameRegex.pattern()
      (firstParameter().valueMatcher as RegexStringMatcher).regex.pattern() == valueRegex.pattern()
  }

  def "with_query_parameter_matching(NamedParameter)"() {
    given:
      def nameMatcher = new StringMatcher() {}
      def valueMatcher = new StringMatcher() {}
      def parameter = new NamedParameter(nameMatcher, valueMatcher)
    when:
      requestBehavior.with_query_parameter_matching(parameter)
    then:
      firstParameter().nameMatcher.is(nameMatcher)
      firstParameter().valueMatcher.is(valueMatcher)
  }

  def "with_header(string)"() {
    given:
      def name = 'name'
    when:
      requestBehavior.with_header(name)
    then:
      (firstHeader().nameMatcher as PlainStringMatcher).plainValue == name
      (firstHeader().valueMatcher as RegexStringMatcher).regex.pattern() == ".*"
  }

  def "with_header(matcher)"() {
    given:
      def matcher = new StringMatcher() {}
    when:
      requestBehavior.with_header(matcher)
    then:
      firstHeader().nameMatcher.is(matcher)
      (firstHeader().valueMatcher as RegexStringMatcher).regex.pattern() == ".*"
  }

  def "with_header(string, string)"() {
    given:
      def name = 'name'
      def value = 'value'
    when:
      requestBehavior.with_header(name, value)
    then:
      (firstHeader().nameMatcher as PlainStringMatcher).plainValue == name
      (firstHeader().valueMatcher as PlainStringMatcher).plainValue == value
  }

  def "with_header(string, matcher)"() {
    given:
      def name = 'name'
      def value = new StringMatcher() {}
    when:
      requestBehavior.with_header(name, value)
    then:
      (firstHeader().nameMatcher as PlainStringMatcher).plainValue == name
      firstHeader().valueMatcher.is(value)
  }

  def "with_header(matcher, string)"() {
    given:
      def name = new StringMatcher() {}
      def value = 'value'
    when:
      requestBehavior.with_header(name, value)
    then:
      firstHeader().nameMatcher.is(name)
      (firstHeader().valueMatcher as PlainStringMatcher).plainValue == value
  }

  def "with_header(matcher, matcher)"() {
    given:
      def name = new StringMatcher() {}
      def value = new StringMatcher() {}
    when:
      requestBehavior.with_header(name, value)
    then:
      firstHeader().nameMatcher.is(name)
      firstHeader().valueMatcher.is(value)
  }

  def "with_header_matching(string)"() {
    given:
      def nameRegex = '.*name'
    when:
      requestBehavior.with_header_matching(nameRegex)
    then:
      (firstHeader().nameMatcher as RegexStringMatcher).regex.pattern() == nameRegex
      (firstHeader().valueMatcher as RegexStringMatcher).regex.pattern() == ".*"
  }

  def "with_header_matching(pattern)"() {
    given:
      def nameRegex = ~/.*name/
    when:
      requestBehavior.with_header_matching(nameRegex)
    then:
      (firstHeader().nameMatcher as RegexStringMatcher).regex.pattern() == nameRegex.pattern()
      (firstHeader().valueMatcher as RegexStringMatcher).regex.pattern() == ".*"
  }

  def "with_header_matching(string, string)"() {
    given:
      def nameRegex = '.*name'
      def valueRegex = '.*value'
    when:
      requestBehavior.with_header_matching(nameRegex, valueRegex)
    then:
      (firstHeader().nameMatcher as RegexStringMatcher).regex.pattern() == nameRegex
      (firstHeader().valueMatcher as RegexStringMatcher).regex.pattern() == valueRegex
  }

  def "with_header_matching(pattern, pattern)"() {
    given:
      def nameRegex = ~/.*name/
      def valueRegex = ~/.*value/
    when:
      requestBehavior.with_header_matching(nameRegex, valueRegex)
    then:
      (firstHeader().nameMatcher as RegexStringMatcher).regex.pattern() == nameRegex.pattern()
      (firstHeader().valueMatcher as RegexStringMatcher).regex.pattern() == valueRegex.pattern()
  }

  def "with_header_matching(NamedParameter)"() {
    given:
      def nameMatcher = new StringMatcher() {}
      def valueMatcher = new StringMatcher() {}
      def parameter = new NamedParameter(nameMatcher, valueMatcher)
    when:
      requestBehavior.with_header_matching(parameter)
    then:
      firstHeader().nameMatcher.is(nameMatcher)
      firstHeader().valueMatcher.is(valueMatcher)
  }

  @TupleConstructor
  private static class TestObject {
    String prop1
    int prop2
  }
}
