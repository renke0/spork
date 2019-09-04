package spork.httpmock.matcher

class NamedParameter {
  final StringMatcher nameMatcher
  final StringMatcher valueMatcher

  NamedParameter(StringMatcher nameMatcher, StringMatcher valueMatcher) {
    this.nameMatcher = nameMatcher
    this.valueMatcher = valueMatcher
  }
}
