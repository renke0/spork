package spork.httpmock.value

import static groovy.json.JsonOutput.toJson
import static spork.httpmock.value.BodyValue.MatchingStrategy.LOOSE
import static spork.httpmock.value.BodyValue.MatchingStrategy.STRICT

abstract class BodyValue {
  static BodyValue loosely(String json) {
    return new JsonBodyValue(json, LOOSE)
  }

  static BodyValue loosely(Map map) {
    loosely(toJson(map))
  }

  static BodyValue loosely(def object) {
    return loosely(toJson(object as Object))
  }

  static BodyValue strictly(String json) {
    return new JsonBodyValue(json, STRICT)
  }

  static BodyValue strictly(Map map) {
    strictly(toJson(map))
  }

  static BodyValue strictly(def object) {
    return strictly(toJson(object as Object))
  }

  BodyValue not() {
    return new NegatedBodyValue(this)
  }

  static enum MatchingStrategy {
    STRICT, LOOSE
  }

  static BodyValue jsonPath(String jsonPath) {
    return new JsonPathBodyValue(jsonPath)
  }

  static class JsonBodyValue extends BodyValue {
    final String json
    final MatchingStrategy strategy

    JsonBodyValue(String json, MatchingStrategy strategy) {
      this.json = json
      this.strategy = strategy
    }
  }

  static class JsonPathBodyValue extends BodyValue {
    final String jsonPath

    JsonPathBodyValue(String jsonPath) {
      this.jsonPath = jsonPath
    }
  }

  static class NegatedBodyValue extends BodyValue implements Negated<BodyValue> {
    final BodyValue wrapped

    NegatedBodyValue(BodyValue wrapped) {
      this.wrapped = wrapped
    }
  }
}
