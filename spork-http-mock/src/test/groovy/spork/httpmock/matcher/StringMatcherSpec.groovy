package spork.httpmock.matcher

import static spork.httpmock.matcher.Matcher.not
import static spork.httpmock.matcher.StringMatcher.NegatedStringMatcher
import static spork.httpmock.matcher.StringMatcher.anyValue
import static spork.httpmock.matcher.StringMatcher.matchingRegex
import static spork.httpmock.matcher.StringMatcher.plainText

import spock.lang.Unroll
import spork.test.SporkSpecification

@Unroll
class StringMatcherSpec extends SporkSpecification {
  def "plainText()"() {
    given:
      def value = randomString()
    when:
      def matcher = plainText(value)
    then:
      matcher.plainValue == value
  }

  def "matchingRegex(#type)"() {
    when:
      def matcher = matchingRegex(regex)
    then:
      matcher.regex.pattern() == regexAsString
    where:
      type      | regex | regexAsString
      'string'  | '.*'  | regex
      'pattern' | ~/.*/ | regex.pattern()
  }

  def "anyValue()"() {
    when:
      def matcher = anyValue()
    then:
      matcher.regex.pattern() == '.*'
  }

  def "not()"() {
    given:
      def matcher = new StringMatcher() {}
    when:
      def negated = not(matcher)
    then:
      negated instanceof NegatedStringMatcher
      (negated as NegatedStringMatcher).matcher.is(matcher)
  }
}
