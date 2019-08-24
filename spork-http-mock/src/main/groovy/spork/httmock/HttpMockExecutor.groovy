package spork.httmock

import static groovy.json.JsonOutput.toJson
import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response
import static spork.internal.PropertyResolver.properties

import groovy.transform.PackageScope
import org.mockserver.client.MockServerClient
import org.mockserver.model.Header
import org.mockserver.model.Parameter
import spork.behavior.BehaviorExecutor
import spork.httmock.HttpMock.HttpMockDefinition

@PackageScope
class HttpMockExecutor implements BehaviorExecutor<HttpMock> {
  private static final PATH_PARAMETER_PATTERN = ~/\{\p{Alnum}+}/
  static final String MOCK_SERVER_URL_PROPERTY = 'spork.http-mock.mock-server.url'
  static final String MOCK_SERVER_PORT_PROPERTY = 'spork.http-mock.mock-server.port'

  @Override
  void execute(HttpMock behavior) {
    def url = properties().propertyAsString(MOCK_SERVER_URL_PROPERTY)
    def port = properties().propertyAsInteger(MOCK_SERVER_PORT_PROPERTY)
    def client = new MockServerClient(url, port)
    behavior.mocks.each {
      client.when(asRequest(it))
        .respond(asResponse(it))
    }
  }

  private asRequest(HttpMockDefinition mock) {
    def request = request()
      .withMethod(mock.requestMethod.name())
      .withPath(buildPath(mock.requestPath, mock.pathParameters))
      .withHeaders(mock.requestHeaders.collect { new Header(it.key, it.value) })
      .withQueryStringParameters(mock.requestParameters.collect { new Parameter(it.key, it.value) })
    if (mock.requestBody)
      request.withBody(toJson(mock.requestBody))
    return request
  }

  private asResponse(HttpMockDefinition mock) {
    def response = response()
      .withStatusCode(mock.responseStatus.code)
      .withHeaders(mock.responseHeaders.collect { new Header(it.key, it.value) })
    if (mock.responseBody)
      response.withBody(toJson(mock.responseBody))
    return response
  }

  private static buildPath(String requestPath, List<String> pathParameters) {
    def path = requestPath
    pathParameters.forEach { path = path.replaceFirst(PATH_PARAMETER_PATTERN, it) }
    return path
  }
}
