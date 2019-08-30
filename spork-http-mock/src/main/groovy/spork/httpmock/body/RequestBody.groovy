package spork.httpmock.body

import static groovy.json.JsonOutput.toJson
import static spork.httpmock.body.JsonBodyMatcher.MatchingStrategy.LOOSE
import static spork.httpmock.body.JsonBodyMatcher.MatchingStrategy.STRICT

class RequestBody {
  static loosely(String json) {
    return new JsonBodyMatcher(json, LOOSE)
  }

  static loosely(Map map) {
    loosely(toJson(map))
  }

  static loosely(def object) {
    return loosely(toJson(object as Object))
  }

  static strictly(String json) {
    return new JsonBodyMatcher(json, STRICT)
  }

  static strictly(Map map) {
    strictly(toJson(map))
  }

  static strictly(def object) {
    return strictly(toJson(object as Object))
  }

  static jsonPath(String jsonPath) {
    return new JsonPathBodyMatcher(jsonPath)
  }
}
