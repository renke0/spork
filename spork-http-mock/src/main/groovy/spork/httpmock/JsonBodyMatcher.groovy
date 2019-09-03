package spork.httpmock

class JsonBodyMatcher implements BodyMatcher {
  final String json
  final MatchingStrategy strategy

  JsonBodyMatcher(String json, MatchingStrategy strategy) {
    this.json = json
    this.strategy = strategy
  }

  enum MatchingStrategy {
    STRICT, LOOSE
  }
}
