package spork.httpmock.integration

import static spork.http.HttpStatus.NOT_FOUND
import static spork.http.HttpStatus.OK
import static spork.httpmock.behavior.HttpMockBehaviorDsl.any_http_request
import static spork.httpmock.behavior.HttpMockBehaviorDsl.httpMock
import static spork.httpmock.behavior.HttpMockBehaviorDsl.will_return_a_response
import static spork.test.HttpClient.getGET
import static spork.test.HttpClient.request

import spock.lang.Unroll
import spork.core.error.TestConfigurationException
import spork.http.HttpMethod
import spork.test.SporkSpecification

@Unroll
abstract class HttpMockAdapterIntegration extends SporkSpecification {
  abstract int getPort()

  def "missing response status"() {
    when:
      httpMock(
          any_http_request {},
          will_return_a_response {})
    then:
      thrown(TestConfigurationException)
  }

  def "request.to_path -> #method"() {
    given:
      def path = "/${randomLowercaseString()}"
      httpMock(
          any_http_request {
            to_path(path)
          },
          will_return_a_response {
            with_status(OK)
          })
    when:
      def response = request(method, localhost(path))
    then:
      response.successful
      response.code() == OK.code
    where:
      method << HttpMethod.values()*.name()
  }

  def "request.to_path_matching -> #regex"() {
    given:
      httpMock(
          any_http_request {
            to_path_matching(regex)
          },
          will_return_a_response {
            with_status(OK)
          })
    when:
      def response = request(GET, localhost('/my-path/my-sub-path'))
    then:
      response.successful == success
      response.code() == code.code
    where:
      regex                | success | code
      '/my-path.*'         | true    | OK
      ~/\/my-path.*/       | true    | OK
      '/my-other-path.*'   | false   | NOT_FOUND
      ~/\/my-other-path.*/ | false   | NOT_FOUND
  }

  def "response.with_status -> #path"() {
    given:
      httpMock(
          any_http_request {},
          will_return_a_response {
            with_status(OK)
          })
    when:
      def response = request(GET, localhost(path))
    then:
      response.successful
      response.body().contentLength() == 0
      response.code() == OK.code
    where:
      path                 | _
      "/${randomString()}" | _
      "/${randomString()}" | _
  }

  private localhost(String path) {
    def relativePath = path.startsWith('/') ? path.substring(1) : path
    "http://localhost:$port/$relativePath"
  }
}
