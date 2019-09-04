package spork.httpmock.behavior

import spork.httpmock.behavior.HttpMockBehaviorDsl.HttpMockRequestBehavior
import spork.httpmock.behavior.HttpMockBehaviorDsl.HttpMockResponseBehavior
import spork.test.SporkSpecification

class HttpMockBehaviorDslSpec extends SporkSpecification {
  def "httpMock()"() {
    given:
      GroovySpy(HttpMockBehaviorDsl, global: true)
      def request = new HttpMockRequestBehavior()
      def response = new HttpMockResponseBehavior()
      def dsl = new HttpMockBehaviorDsl()
      new HttpMockBehaviorDsl() >> dsl
    when:
      HttpMockBehaviorDsl.httpMock(request, response)
    then:
      1 * dsl.setup(_) >> {
        HttpMockBehavior behavior ->
          assert behavior.request == request.request
          assert behavior.response == response.response
      }
  }

  def "processorType()"() {
    when:
      def type = new HttpMockBehaviorDsl().processorType()
    then:
      type == HttpMockProcessor
  }
}


