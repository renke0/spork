package spork.httpmock.matcher

interface Negated<T extends Matcher> {
  T getMatcher()
}
