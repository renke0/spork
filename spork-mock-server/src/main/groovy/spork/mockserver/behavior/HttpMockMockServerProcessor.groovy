package spork.mockserver.behavior

import static spork.core.internal.ApplicationProperties.properties

import org.mockserver.client.MockServerClient
import spork.httpmock.behavior.HttpMockBehavior
import spork.httpmock.behavior.HttpMockProcessor

class HttpMockMockServerProcessor implements HttpMockProcessor {
  private final MockServerClient client

  HttpMockMockServerProcessor() {
    def url = properties().getPropertyAsString('spork.http-mock.mock-server.url')
    def port = properties().getPropertyAsInteger('spork.http-mock.mock-server.port')
    client = new MockServerClient(url, port)
  }

  @Override
  Object setup(HttpMockBehavior behavior) {
    client.when(new RequestBuilder(behavior.request).build())
      .respond(new ResponseBuilder(behavior.response).build())
    return null
  }

  @Override
  void reset() {
    client.reset()
  }
}
