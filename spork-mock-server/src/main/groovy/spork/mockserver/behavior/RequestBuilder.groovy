package spork.mockserver.behavior

import static org.mockserver.matchers.MatchType.ONLY_MATCHING_FIELDS
import static org.mockserver.matchers.MatchType.STRICT
import static org.mockserver.model.JsonBody.json
import static org.mockserver.model.JsonPathBody.jsonPath
import static org.mockserver.model.NottableString.not
import static org.mockserver.model.NottableString.string

import groovy.transform.PackageScope
import org.mockserver.model.Body
import org.mockserver.model.Headers
import org.mockserver.model.HttpRequest
import org.mockserver.model.JsonBody
import org.mockserver.model.KeysToMultiValues
import org.mockserver.model.NottableString
import org.mockserver.model.Parameters
import spork.core.error.TestConfigurationException
import spork.httpmock.behavior.HttpMockRequest
import spork.httpmock.value.BodyValue
import spork.httpmock.value.BodyValue.JsonBodyValue
import spork.httpmock.value.BodyValue.JsonPathBodyValue
import spork.httpmock.value.BodyValue.MatchingStrategy
import spork.httpmock.value.NamedParameter
import spork.httpmock.value.Negated
import spork.httpmock.value.StringValue
import spork.httpmock.value.StringValue.PlainStringValue
import spork.httpmock.value.StringValue.RegexStringValue

@PackageScope
class RequestBuilder {
  final HttpMockRequest mockRequest

  RequestBuilder(HttpMockRequest mockRequest) {
    this.mockRequest = mockRequest
  }

  HttpRequest build() {
    return HttpRequest.request()
        .withMethod(requestMethod() as NottableString)
        .withPath(path() as NottableString)
        .withHeaders(headers() as Headers)
        .withQueryStringParameters(queryParameters() as Parameters)
        .withBody(body() as Body)
  }

  private NottableString requestMethod() {
    return resolveStringValue(mockRequest.method)
  }

  private NottableString path() {
    return resolveStringValue(mockRequest.path)
  }

  private Headers headers() {
    return mockRequest
        .headers
        .inject(new Headers()) { h, n -> resolveKeyValue(h, n) }
  }

  private Parameters queryParameters() {
    return mockRequest
        .queryParameters
        .inject(new Parameters()) { p, n -> resolveKeyValue(p, n) }
  }

  private Body body() {
    return resolveBody(mockRequest.bodyValue)
  }

  private static Body jsonBody(JsonBodyValue bodyValue) {
    def matchType = bodyValue.strategy == MatchingStrategy.STRICT ? STRICT : ONLY_MATCHING_FIELDS
    return json(bodyValue.json, matchType)
  }

  private static Body jsonPathBody(JsonPathBodyValue bodyValue) {
    return jsonPath(bodyValue.jsonPath)
  }

  private static <T extends KeysToMultiValues> T resolveKeyValue(T holder, NamedParameter parameter) {
    def key = resolveStringValue(parameter.key)
    def value = resolveStringValue(parameter.value)
    return holder.withEntry(key, value) as T
  }

  private static NottableString resolveStringValue(StringValue stringValue) {
    if (stringValue) {
      def resolution = resolveNegation(stringValue)
      def value = asStringNotation(resolution.value as StringValue)
      return resolution.negate ? not(value) : string(value)
    }
    return null
  }

  private static Body resolveBody(BodyValue bodyValue) {
    if (bodyValue) {
      def resolution = resolveNegation(bodyValue)
      def value = asBody(resolution.value as BodyValue)
      return JsonBody.not(value, resolution.negate)
    }
    return null
  }

  private static resolveNegation(def negated) {
    def negate = false
    def actual = negated
    while (actual instanceof Negated) {
      actual = (actual as Negated).wrapped
      negate = !negate
    }
    return [value: actual, negate: negate]
  }

  private static String asStringNotation(StringValue stringValue) {
    switch (stringValue.class) {
      case PlainStringValue:
        return (stringValue as PlainStringValue).plainValue
      case RegexStringValue:
        return (stringValue as RegexStringValue).regex.pattern()
      default:
        throw new TestConfigurationException("String value type not known: ${stringValue.class}")
    }
  }

  private static Body asBody(BodyValue bodyValue) {
    switch (bodyValue.class) {
      case JsonBodyValue:
        return jsonBody(bodyValue as JsonBodyValue)
      case JsonPathBodyValue:
        return jsonPathBody(bodyValue as JsonPathBodyValue)
      default:
        throw new TestConfigurationException("Body value type not known: ${bodyValue.class}")
    }
  }
}
