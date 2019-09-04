package spork.httpmock.behavior

import static spork.httpmock.matcher.BodyMatcher.loosely
import static spork.httpmock.matcher.BodyMatcher.strictly
import static spork.httpmock.matcher.StringMatcher.anyValue
import static spork.httpmock.matcher.StringMatcher.matchingRegex
import static spork.httpmock.matcher.StringMatcher.plainText

import groovy.json.JsonOutput
import java.util.regex.Pattern
import spork.core.behavior.BehaviorDsl
import spork.core.behavior.BehaviorProcessor
import spork.http.HttpStatus
import spork.httpmock.matcher.BodyMatcher
import spork.httpmock.matcher.NamedParameter
import spork.httpmock.matcher.StringMatcher

class HttpMockBehaviorDsl extends BehaviorDsl {

  static void httpMock(HttpMockRequestBehavior requestBehavior, HttpMockResponseBehavior responseBehavior) {
    def behavior = new HttpMockBehavior(requestBehavior.request, responseBehavior.response)
    new HttpMockBehaviorDsl().setup(behavior)
  }

  @Override
  protected Class<? extends BehaviorProcessor> processorType() {
    return HttpMockProcessor
  }

  def static any_http_request(@DelegatesTo(HttpMockRequestBehavior) Closure closure) {
    delegate(new HttpMockRequestBehavior(), closure)
  }

  def static will_return_a_response(@DelegatesTo(HttpMockResponseBehavior) Closure closure) {
    delegate(new HttpMockResponseBehavior(), closure)
  }

  private static paramList(def object) {
    if (object instanceof Collection)
      return object.collect { String.valueOf(it) }
    else
      return [String.valueOf(object as Object)]
  }

  static class HttpMockRequestBehavior {
    final HttpMockRequest request = new HttpMockRequest()

    void with_body_loosely_matching(Map body) {
      with_body_matching(loosely(body))
    }

    void with_body_loosely_matching(String body) {
      with_body_matching(loosely(body))
    }

    void with_body_loosely_matching(Object body) {
      with_body_matching(loosely(body))
    }

    void with_body_strictly_matching(Map body) {
      with_body_matching(strictly(body))
    }

    void with_body_strictly_matching(String body) {
      with_body_matching(strictly(body))
    }

    void with_body_strictly_matching(Object body) {
      with_body_matching(strictly(body))
    }

    void with_body_matching(BodyMatcher bodyMatcher) {
      request.bodyMatcher = bodyMatcher
    }

    void to_path(String path) {
      to_path_matching(plainText(path))
    }

    void to_path_matching(String regex) {
      to_path_matching(matchingRegex(regex))
    }

    void to_path_matching(Pattern pattern) {
      to_path_matching(matchingRegex(pattern))
    }

    void to_path_matching(StringMatcher matcher) {
      request.path = matcher
    }

    void with_query_parameter(String name) {
      with_query_parameter_matching(new NamedParameter(plainText(name), anyValue()))
    }

    void with_query_parameter(StringMatcher name) {
      with_query_parameter_matching(new NamedParameter(name, anyValue()))
    }

    void with_query_parameter(String name, String value) {
      with_query_parameter_matching(new NamedParameter(plainText(name), plainText(value)))
    }

    void with_query_parameter(StringMatcher name, String value) {
      with_query_parameter_matching(new NamedParameter(name, plainText(value)))
    }

    void with_query_parameter(String name, StringMatcher value) {
      with_query_parameter_matching(new NamedParameter(plainText(name), value))
    }

    void with_query_parameter(StringMatcher name, StringMatcher value) {
      with_query_parameter_matching(new NamedParameter(name, value))
    }

    void with_query_parameter_matching(String nameRegex) {
      with_query_parameter_matching(new NamedParameter(matchingRegex(nameRegex), anyValue()))
    }

    void with_query_parameter_matching(Pattern nameRegex) {
      with_query_parameter_matching(new NamedParameter(matchingRegex(nameRegex), anyValue()))
    }

    void with_query_parameter_matching(String nameRegex, String valueRegex) {
      with_query_parameter_matching(new NamedParameter(matchingRegex(nameRegex), matchingRegex(valueRegex)))
    }

    void with_query_parameter_matching(Pattern nameRegex, Pattern valueRegex) {
      with_query_parameter_matching(new NamedParameter(matchingRegex(nameRegex), matchingRegex(valueRegex)))
    }

    void with_query_parameter_matching(NamedParameter queryParameter) {
      request.queryParameters << queryParameter
    }

    void with_header(String name) {
      with_header_matching(new NamedParameter(plainText(name), anyValue()))
    }

    void with_header(StringMatcher name) {
      with_header_matching(new NamedParameter(name, anyValue()))
    }

    void with_header(String name, String value) {
      with_header_matching(new NamedParameter(plainText(name), plainText(value)))
    }

    void with_header(StringMatcher name, String value) {
      with_header_matching(new NamedParameter(name, plainText(value)))
    }

    void with_header(String name, StringMatcher value) {
      with_header_matching(new NamedParameter(plainText(name), value))
    }

    void with_header(StringMatcher name, StringMatcher value) {
      with_header_matching(new NamedParameter(name, value))
    }

    void with_header_matching(String nameRegex) {
      with_header_matching(new NamedParameter(matchingRegex(nameRegex), anyValue()))
    }

    void with_header_matching(Pattern nameRegex) {
      with_header_matching(new NamedParameter(matchingRegex(nameRegex), anyValue()))
    }

    void with_header_matching(String nameRegex, String valueRegex) {
      with_header_matching(new NamedParameter(matchingRegex(nameRegex), matchingRegex(valueRegex)))
    }

    void with_header_matching(Pattern nameRegex, Pattern valueRegex) {
      with_header_matching(new NamedParameter(matchingRegex(nameRegex), matchingRegex(valueRegex)))
    }

    void with_header_matching(NamedParameter header) {
      request.headers << header
    }
  }

  static class HttpMockResponseBehavior {
    final HttpMockResponse response = new HttpMockResponse()

    void with_status(HttpStatus status) {
      response.status = status
    }

    void with_body(Object body) {
      with_body([*: body.class.declaredFields
        .findAll { !it.synthetic }
        .collectEntries { [it.name, body."$it.name"] }])
    }

    void with_body(Map map) {
      with_body(JsonOutput.toJson(map))
    }

    void with_body(String body) {
      response.body = body
    }

    void with_header(String name, def value) {
      response.headers.put(name, paramList(value))
    }
  }
}
