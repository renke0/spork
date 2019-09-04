package spork.httpmock.matcher

import static groovy.json.JsonOutput.toJson
import static spork.httpmock.matcher.BodyMatcher.MatchingStrategy.LOOSE
import static spork.httpmock.matcher.BodyMatcher.MatchingStrategy.STRICT

abstract class BodyMatcher extends Matcher {
  static JsonBodyMatcher loosely(String json) {
    return new JsonBodyMatcher(json, LOOSE)
  }

  static JsonBodyMatcher loosely(Map map) {
    loosely(toJson(map))
  }

  static JsonBodyMatcher loosely(def object) {
    return loosely(toJson(object as Object))
  }

  static JsonBodyMatcher strictly(String json) {
    return new JsonBodyMatcher(json, STRICT)
  }

  static JsonBodyMatcher strictly(Map map) {
    strictly(toJson(map))
  }

  static JsonBodyMatcher strictly(def object) {
    return strictly(toJson(object as Object))
  }

  static JsonPathBodyMatcher jsonPath(String jsonPath) {
    return new JsonPathBodyMatcher(jsonPath)
  }

  static enum MatchingStrategy {
    STRICT, LOOSE
  }

  @Override
  NegatedBodyMatcher negated() {
    return new NegatedBodyMatcher(this)
  }

  static class JsonBodyMatcher extends BodyMatcher {
    final String json
    final MatchingStrategy strategy

    JsonBodyMatcher(String json, MatchingStrategy strategy) {
      this.json = json
      this.strategy = strategy
    }
  }

  static class JsonPathBodyMatcher extends BodyMatcher {
    final String jsonPath

    JsonPathBodyMatcher(String jsonPath) {
      this.jsonPath = jsonPath
    }
  }

  static class NegatedBodyMatcher extends BodyMatcher implements Negated<BodyMatcher> {
    final BodyMatcher matcher

    NegatedBodyMatcher(BodyMatcher matcher) {
      this.matcher = matcher
    }
  }
}
