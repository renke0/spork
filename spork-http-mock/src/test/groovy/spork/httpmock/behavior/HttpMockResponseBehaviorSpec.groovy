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

  def "with_body(#type)"() {
    when:
      responseBehavior.with_body(body)
    then:
      json(responseBehavior.response.body) == json(bodyString)
    where:
      type     | body                                    | bodyString
      'map'    | ['prop1': 'val1', 'prop2': 2]           | '{"prop1":"val1","prop2":2}'
      'object' | new TestObject(prop1: 'val1', prop2: 2) | '{"prop1":"val1","prop2":2}'
      'json'   | '{"prop1":"val1","prop2":2}'            | '{"prop1":"val1","prop2":2}'
  }

  def "with_header() -> #label"() {
    given:
      def name = 'name'
    when:
      responseBehavior.with_header(name, value)
    then:
      responseBehavior.response.headers.containsKey(name)
      responseBehavior.response.headers.get(name) == expectation
    where:
      label      | value                | expectation
      'single'   | 'value'              | ['value']
      'multiple' | ['value1', 'value2'] | value
  }

  @TupleConstructor
  private static class TestObject {
    String prop1
    int prop2
  }
}
