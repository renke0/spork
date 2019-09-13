package spork.mockserver.behavior

import static org.mockserver.integration.ClientAndServer.startClientAndServer
import static spork.core.internal.ApplicationProperties.properties
import static spork.http.HttpStatus.OK
import static spork.httpmock.behavior.HttpMockBehaviorDsl.any_http_request
import static spork.httpmock.behavior.HttpMockBehaviorDsl.httpMock
import static spork.httpmock.behavior.HttpMockBehaviorDsl.will_return_a_response
import static spork.test.HttpClient.GET
import static spork.test.HttpClient.request

import org.mockserver.integration.ClientAndServer
import spock.lang.Shared
import spock.lang.Unroll
import spork.core.error.TestConfigurationException
import spork.http.HttpMethod
import spork.test.SporkSpecification

@Unroll
class HttpMockMockServerAdapterSpec extends SporkSpecification {
  @Shared
  private ClientAndServer mockServer
  @Shared
  private int port

  def setupSpec() {
    port = properties().getAsInteger('spork.http-mock.mock-server.port')
    mockServer = startClientAndServer(port)
  }

  def cleanupSpec() {
    mockServer.stop()
  }

  def cleanup() {
    mockServer.reset()
  }

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

  String localhost(String path) {
    def relativePath = path.startsWith('/') ? path.substring(1) : path
    "http://localhost:$port/$relativePath"
  }
}
