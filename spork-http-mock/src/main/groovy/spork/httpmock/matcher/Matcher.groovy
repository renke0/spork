package spork.httpmock.matcher

abstract class Matcher {
  abstract <T extends Matcher> T negated()

  static <T extends Matcher> T not(T matcher) {
    return matcher.negated()
  }
}
