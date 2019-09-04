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
import spork.error.TestConfigurationException
import spork.httpmock.behavior.HttpMockRequest
import spork.httpmock.matcher.BodyMatcher
import spork.httpmock.matcher.BodyMatcher.JsonBodyMatcher
import spork.httpmock.matcher.BodyMatcher.JsonPathBodyMatcher
import spork.httpmock.matcher.BodyMatcher.MatchingStrategy
import spork.httpmock.matcher.Matcher
import spork.httpmock.matcher.NamedParameter
import spork.httpmock.matcher.Negated
import spork.httpmock.matcher.StringMatcher
import spork.httpmock.matcher.StringMatcher.PlainStringMatcher
import spork.httpmock.matcher.StringMatcher.RegexStringMatcher

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
    return resolveStringMatcher(mockRequest.method)
  }

  private NottableString path() {
    return resolveStringMatcher(mockRequest.path)
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
    return resolveBody(mockRequest.bodyMatcher)
  }

  private static Body jsonBody(JsonBodyMatcher jsonBodyMatcher) {
    def matchType = jsonBodyMatcher.strategy == MatchingStrategy.STRICT ? STRICT : ONLY_MATCHING_FIELDS
    return json(jsonBodyMatcher.json, matchType)
  }

  private static Body jsonPathBody(JsonPathBodyMatcher jsonPathBodyMatcher) {
    return jsonPath(jsonPathBodyMatcher.jsonPath)
  }

  private static <T extends KeysToMultiValues> T resolveKeyValue(T holder, NamedParameter parameter) {
    def key = resolveStringMatcher(parameter.nameMatcher)
    def value = resolveStringMatcher(parameter.valueMatcher)
    return holder.withEntry(key, value) as T
  }

  private static NottableString resolveStringMatcher(StringMatcher matcher) {
    if (matcher) {
      def resolution = resolveNegation(matcher)
      def value = asStringNotation(resolution.matcher as StringMatcher)
      return resolution.negate ? not(value) : string(value)
    }
    return null
  }

  private static Body resolveBody(BodyMatcher bodyMatcher) {
    if (bodyMatcher) {
      def resolution = resolveNegation(bodyMatcher)
      def value = asBody(resolution.matcher as BodyMatcher)
      return JsonBody.not(value, resolution.negate)
    }
    return null
  }

  private static resolveNegation(Matcher matcher) {
    def negate = false
    def actualMatcher = matcher
    while (actualMatcher instanceof Negated) {
      actualMatcher = (actualMatcher as Negated).matcher
      negate = !negate
    }
    return [matcher: actualMatcher, negate: negate]
  }

  private static String asStringNotation(StringMatcher stringMatcher) {
    switch (stringMatcher.class) {
      case PlainStringMatcher:
        return (stringMatcher as PlainStringMatcher).plainValue
      case RegexStringMatcher:
        return (stringMatcher as RegexStringMatcher).regex.pattern()
      default:
        throw new TestConfigurationException("String matcher type not known: ${stringMatcher.class}")
    }
  }

  private static Body asBody(BodyMatcher bodyMatcher) {
    switch (bodyMatcher.class) {
      case JsonBodyMatcher:
        return jsonBody(bodyMatcher as JsonBodyMatcher)
      case JsonPathBodyMatcher:
        return jsonPathBody(bodyMatcher as JsonPathBodyMatcher)
      default:
        throw new TestConfigurationException("Body matcher type not known: ${bodyMatcher.class}")
    }
  }
}
