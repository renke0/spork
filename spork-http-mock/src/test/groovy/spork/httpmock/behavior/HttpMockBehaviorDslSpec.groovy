package spork.httpmock.behavior

import spork.core.error.TestConfigurationException
import spork.http.HttpStatus
import spork.httpmock.behavior.HttpMockBehaviorDsl.HttpMockRequestBehavior
import spork.httpmock.behavior.HttpMockBehaviorDsl.HttpMockResponseBehavior
import spork.test.SporkSpecification

class HttpMockBehaviorDslSpec extends SporkSpecification {
  def "httpMock()"() {
    given:
      GroovySpy(HttpMockBehaviorDsl, global: true)
      def request = new HttpMockRequestBehavior()
      def response = new HttpMockResponseBehavior()
      response.with_status(randomItem(HttpStatus.values()))
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

  def "httpMock() -> no status"() {
    given:
      def request = new HttpMockRequestBehavior()
      def response = new HttpMockResponseBehavior()
    when:
      HttpMockBehaviorDsl.httpMock(request, response)
    then:
      thrown(TestConfigurationException)
  }

  def "adapterType()"() {
    when:
      def type = new HttpMockBehaviorDsl().adapterType()
    then:
      type == HttpMockAdapter
  }

  def "any_http_request()"() {
    given:
      def closure = {}
    when:
      def request = HttpMockBehaviorDsl.any_http_request(closure)
    then:
      request instanceof HttpMockRequestBehavior
      closure.delegate == request
  }

  def "will_return_a_response()"() {
    given:
      def closure = {}
    when:
      def response = HttpMockBehaviorDsl.will_return_a_response(closure)
    then:
      response instanceof HttpMockResponseBehavior
      closure.delegate == response
  }
}


