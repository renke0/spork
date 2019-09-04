package spork.sandbox

import static spork.httpmock.behavior.HttpMockBehaviorDsl.any_http_request
import static spork.httpmock.behavior.HttpMockBehaviorDsl.httpMock
import static spork.httpmock.behavior.HttpMockBehaviorDsl.will_return_a_response

import org.junit.jupiter.api.Disabled
import spork.core.lang.Integration
import spork.http.HttpStatus

@Disabled
class MyFirstIntegration extends Integration {

  def test() {
    when:
      httpMock(
        any_http_request {
          to_path('/mypath')
        },
        will_return_a_response {
          with_status(HttpStatus.OK)
          with_body([hi: "I'm a body"])
        })

      def connection = new URL('http://localhost:1080/mypath').openConnection()
      print(connection.inputStream.text)

    then:
      1 < 2
  }

  def test2() {
    when:
      httpMock(
        any_http_request {
          to_path('/mypath')
        },
        will_return_a_response {
          with_status(HttpStatus.OK)
          with_body([hi: "I'm another body"])
        })

      def connection = new URL('http://localhost:1080/mypath').openConnection()
      print(connection.inputStream.text)

    then:
      1 < 2
  }
}
