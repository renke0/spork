package spork.httpmock.matcher

import java.util.regex.Pattern

abstract class StringMatcher extends Matcher {
  static PlainStringMatcher plainText(String value) {
    return new PlainStringMatcher(value)
  }

  static RegexStringMatcher matchingRegex(String regex) {
    return matchingRegex(Pattern.compile(regex))
  }

  static RegexStringMatcher matchingRegex(Pattern regex) {
    return new RegexStringMatcher(regex)
  }

  static RegexStringMatcher anyValue() {
    return new RegexStringMatcher(~/.*/)
  }

  @Override
  NegatedStringMatcher negated() {
    return new NegatedStringMatcher(this)
  }

  static class PlainStringMatcher extends StringMatcher {
    final String plainValue

    PlainStringMatcher(String plainValue) {
      this.plainValue = plainValue
    }
  }

  static class RegexStringMatcher extends StringMatcher {
    final Pattern regex

    RegexStringMatcher(Pattern regex) {
      this.regex = regex
    }
  }

  static class NegatedStringMatcher extends StringMatcher implements Negated<StringMatcher>{
    final StringMatcher matcher

    NegatedStringMatcher(StringMatcher matcher) {
      this.matcher = matcher
    }
  }
}
