package spork.httpmock.matcher

import static spork.httpmock.matcher.BodyMatcher.MatchingStrategy.LOOSE
import static spork.httpmock.matcher.BodyMatcher.MatchingStrategy.STRICT
import static spork.httpmock.matcher.BodyMatcher.NegatedBodyMatcher
import static spork.httpmock.matcher.Matcher.not

import groovy.transform.TupleConstructor
import spock.lang.Unroll
import spork.test.SporkSpecification

@Unroll
class BodyMatcherSpec extends SporkSpecification {
  def "loosely(#type)"() {
    when:
      def matcher = BodyMatcher.loosely(body)
    then:
      json(matcher.json) == json(expectation)
      matcher.strategy == LOOSE
    where:
      type     | body                                    | expectation
      'map'    | ['prop1': 'val1', 'prop2': 2]           | '{"prop1":"val1","prop2":2}'
      'object' | new TestObject(prop1: 'val1', prop2: 2) | '{"prop1":"val1","prop2":2}'
      'json'   | '{"prop1":"val1","prop2":2}'            | '{"prop1":"val1","prop2":2}'
  }

  def "strictly(#type)"() {
    when:
      def matcher = BodyMatcher.strictly(body)
    then:
      json(matcher.json) == json(expectation)
      matcher.strategy == STRICT
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
      def matcher = BodyMatcher.jsonPath(jsonPath)
    then:
      matcher.jsonPath == jsonPath
  }

  def "not()"() {
    given:
      def matcher = new BodyMatcher() {}
    when:
      def negated = not(matcher)
    then:
      negated instanceof NegatedBodyMatcher
      (negated as NegatedBodyMatcher).matcher == matcher
  }

  @TupleConstructor
  private static class TestObject {
    String prop1
    int prop2
  }
}
