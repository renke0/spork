package spork.mockserver.behavior

import groovy.json.JsonOutput
import groovy.transform.PackageScope
import org.mockserver.model.Headers
import org.mockserver.model.HttpResponse
import org.mockserver.model.JsonBody
import spork.httpmock.behavior.HttpMockResponse

@PackageScope
class ResponseBuilder {
  final HttpMockResponse mockResponse

  ResponseBuilder(HttpMockResponse mockResponse) {
    this.mockResponse = mockResponse
  }

  HttpResponse build() {
    return new HttpResponse()
        .withStatusCode(statusCode())
        .withBody(body() as JsonBody)
        .withHeaders(headers() as Headers)
  }

  private int statusCode() {
    return mockResponse.status.code
  }

  private JsonBody body() {
    if (mockResponse.body) {
      return new JsonBody(new JsonOutput().toJson(mockResponse.body))
    }
    return null
  }

  private Headers headers() {
    return mockResponse
        .headers
        .inject(new Headers()) { header, entry -> header.withEntry(entry.key, entry.value as String[]) }
  }
}
