package spork.httpmock.value

import static spork.httpmock.value.BodyValue.JsonBodyValue
import static spork.httpmock.value.BodyValue.JsonPathBodyValue
import static spork.httpmock.value.BodyValue.MatchingStrategy.LOOSE
import static spork.httpmock.value.BodyValue.MatchingStrategy.STRICT
import static spork.httpmock.value.BodyValue.NegatedBodyValue

import groovy.transform.TupleConstructor
import spock.lang.Unroll
import spork.test.SporkSpecification

@Unroll
class BodyValueSpec extends SporkSpecification {
  def "loosely(#type)"() {
    when:
      def bodyValue = BodyValue.loosely(body)
    then:
      bodyValue instanceof JsonBodyValue
      json((bodyValue as JsonBodyValue).json) == json(expectation)
      (bodyValue as JsonBodyValue).strategy == LOOSE
    where:
      type     | body                                    | expectation
      'map'    | ['prop1': 'val1', 'prop2': 2]           | '{"prop1":"val1","prop2":2}'
      'object' | new TestObject(prop1: 'val1', prop2: 2) | '{"prop1":"val1","prop2":2}'
      'json'   | '{"prop1":"val1","prop2":2}'            | '{"prop1":"val1","prop2":2}'
  }

  def "strictly(#type)"() {
    when:
      def bodyValue = BodyValue.strictly(body)
    then:
      bodyValue instanceof JsonBodyValue
      json((bodyValue as JsonBodyValue).json) == json(expectation)
      (bodyValue as JsonBodyValue).strategy == STRICT
    where:
      type     | body                                    | expectation
      'map'    | ['prop1': 'val1', 'prop2': 2]           | '{"prop1":"val1","prop2":2}'
      'object' | new TestObject(prop1: 'val1', prop2: 2) | '{"prop1":"val1","prop2":2}'
      'json'   | '{"prop1":"val1","prop2":2}'            | '{"prop1":"val1","prop2":2}'
  }

  def "jsonPath()"() {
    given:
      def jsonPath = '$.hi'
    when:
      def bodyValue = BodyValue.jsonPath(jsonPath)
    then:
      bodyValue instanceof JsonPathBodyValue
      (bodyValue as JsonPathBodyValue).jsonPath == jsonPath
  }

  def "not()"() {
    given:
      def bodyValue = new BodyValue() {}
    when:
      def negated = bodyValue.not()
    then:
      negated instanceof NegatedBodyValue
      (negated as NegatedBodyValue).wrapped == bodyValue
  }

  @TupleConstructor
  private static class TestObject {
    String prop1
    int prop2
  }
}
