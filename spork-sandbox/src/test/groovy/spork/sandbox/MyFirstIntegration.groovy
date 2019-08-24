package spork.sandbox

import static spork.httmock.HttpMock.mock
import static spork.http.HttpMethod.POST
import static spork.http.HttpStatus.NOT_FOUND

import spork.lang.Integration

class MyFirstIntegration extends Integration {

  def test() {
    when:
      mock {
        any_http_request_to POST, "/mypath", {
          with_body([
            hi: 1,
            hello: [
              howdy: 'ok'
            ]
          ])
          will_return_a_response_with_status(NOT_FOUND)
        }
      }
    then:
      1 < 2
  }
}
