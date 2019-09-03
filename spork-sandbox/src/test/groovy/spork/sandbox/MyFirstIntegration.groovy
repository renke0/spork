package spork.sandbox

import static spork.httpmock.RequestBody.loosely
import static spork.httpmock.behavior.HttpMockBehaviorDsl.any_http_request
import static spork.httpmock.behavior.HttpMockBehaviorDsl.httpMock
import static spork.httpmock.behavior.HttpMockBehaviorDsl.will_return_a_response

import spork.http.HttpStatus
import spork.lang.Integration

class MyFirstIntegration extends Integration {

  def test() {
    when:
      httpMock(
        any_http_request {
          to_path('')
          with_body_matching(loosely([:]))
        },
        will_return_a_response {
          with_status(HttpStatus.NO_CONTENT)
        })

    then:
      1 < 2
  }
}
