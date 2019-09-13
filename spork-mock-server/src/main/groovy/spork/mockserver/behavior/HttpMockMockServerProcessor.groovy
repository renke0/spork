package spork.mockserver.behavior

import static spork.core.internal.ApplicationProperties.properties

import org.mockserver.client.MockServerClient
import spork.httpmock.behavior.HttpMockBehavior
import spork.httpmock.behavior.HttpMockProcessor

class HttpMockMockServerProcessor implements HttpMockProcessor {
  private static final LOCALHOST = 'localhost'
  private final MockServerClient client

  HttpMockMockServerProcessor() {
    def url = properties().getAsString('spork.http-mock.mock-server.url', LOCALHOST)
    def port = properties().getAsInteger('spork.http-mock.mock-server.port')
    client = new MockServerClient(url, port)
  }

  @Override
  Object setup(HttpMockBehavior behavior) {
    client.when(new RequestBuilder(behavior.request).build())
      .respond(new ResponseBuilder(behavior.response).build())
    return behavior
  }

  @Override
  void reset() {
    client.reset()
  }
}
