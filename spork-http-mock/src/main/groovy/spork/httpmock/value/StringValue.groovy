package spork.httpmock.value

import java.util.regex.Pattern

abstract class StringValue {

  static StringValue plainText(String value) {
    return new PlainStringValue(value)
  }

  static StringValue matchingRegex(String regex) {
    return matchingRegex(Pattern.compile(regex))
  }

  static StringValue matchingRegex(Pattern regex) {
    return new RegexStringValue(regex)
  }

  static StringValue anyValue() {
    return new RegexStringValue(~/.*/)
  }

  StringValue not() {
    return new NegatedStringValue(this)
  }

  static class PlainStringValue extends StringValue {
    final String plainValue

    PlainStringValue(String plainValue) {
      this.plainValue = plainValue
    }
  }

  static class RegexStringValue extends StringValue {
    final Pattern regex

    RegexStringValue(Pattern regex) {
      this.regex = regex
    }
  }

  static class NegatedStringValue extends StringValue implements Negated<StringValue> {
    final StringValue wrapped

    NegatedStringValue(StringValue wrapped) {
      this.wrapped = wrapped
    }
  }
}
