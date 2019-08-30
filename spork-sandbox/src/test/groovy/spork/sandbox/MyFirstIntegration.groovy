package spork.sandbox

import static spork.httpmock.HttpMockBehaviorDsl.any_http_request
import static spork.httpmock.HttpMockBehaviorDsl.httpMock
import static spork.httpmock.HttpMockBehaviorDsl.will_return_a_response
import static spork.httpmock.body.RequestBody.loosely

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
