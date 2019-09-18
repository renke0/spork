package spork.mockserver.behavior

import static org.mockserver.integration.ClientAndServer.startClientAndServer
import static spork.core.internal.ApplicationProperties.properties

import org.mockserver.integration.ClientAndServer
import spock.lang.Shared
import spock.lang.Unroll
import spork.httpmock.integration.HttpMockAdapterIntegration

@Unroll
class MockServerAdapterIntegration extends HttpMockAdapterIntegration {
  @Shared
  private ClientAndServer mockServer
  @Shared
  private int mockServerPort

  def setupSpec() {
    mockServerPort = properties().getAsInteger('spork.http-mock.mock-server.port')
    mockServer = startClientAndServer(mockServerPort)
  }

  def cleanupSpec() {
    mockServer.stop()
  }

  def cleanup() {
    mockServer.reset()
  }

  @Override
  int getPort() {
    return mockServerPort
  }
}
