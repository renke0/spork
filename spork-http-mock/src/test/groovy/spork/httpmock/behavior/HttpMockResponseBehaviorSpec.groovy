package spork.httpmock.behavior

import groovy.transform.TupleConstructor
import spock.lang.Subject
import spock.lang.Unroll
import spork.http.HttpStatus
import spork.httpmock.behavior.HttpMockBehaviorDsl.HttpMockResponseBehavior
import spork.test.SporkSpecification

@Unroll
class HttpMockResponseBehaviorSpec extends SporkSpecification {
  @Subject
  private responseBehavior = new HttpMockResponseBehavior()

  def "with_status()"() {
    given:
      def status = randomItem(HttpStatus.values())
    when:
      responseBehavior.with_status(status)
    then:
      responseBehavior.response.status == status
  }

  def "with_body(#body)"() {
    when:
      responseBehavior.with_body(body)
    then:
      responseBehavior.response.body == bodyString
    where:
      body                                    | bodyString
      ['prop1': 'val1', 'prop2': 2]           | '{"prop1":"val1","prop2":2}'
      new TestObject(prop1: 'val1', prop2: 2) | '{"prop1":"val1","prop2":2}'
      '{"prop1":"val1","prop2":2}'            | '{"prop1":"val1","prop2":2}'
  }

  def "with_header()"() {
    given:
      def name = 'name'
      def value = 'value'
    when:
      responseBehavior.with_header(name, value)
    then:
      responseBehavior.response.headers.containsKey(name)
      responseBehavior.response.headers.get(name) == [value]
  }

  @TupleConstructor
  private static class TestObject {
    String prop1
    int prop2

    @Override
    String toString() {
      return "(prop1: $prop1, prop2: $prop2)"
    }
  }
}
