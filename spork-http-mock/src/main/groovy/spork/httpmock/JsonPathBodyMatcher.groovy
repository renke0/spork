package spork.httpmock

class JsonPathBodyMatcher implements BodyMatcher {
  final String jsonPath

  JsonPathBodyMatcher(String jsonPath) {
    this.jsonPath = jsonPath
  }
}
