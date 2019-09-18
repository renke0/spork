package spork.httpmock.value

import static StringValue.anyValue
import static StringValue.matchingRegex
import static StringValue.plainText
import static spork.httpmock.value.StringValue.NegatedStringValue
import static spork.httpmock.value.StringValue.PlainStringValue
import static spork.httpmock.value.StringValue.RegexStringValue

import spock.lang.Unroll
import spork.test.SporkSpecification

@Unroll
class StringValueSpec extends SporkSpecification {
  def "plainText()"() {
    given:
      def value = randomString()
    when:
      def stringValue = plainText(value)
    then:
      stringValue instanceof PlainStringValue
      (stringValue as PlainStringValue).plainValue == value
  }

  def "matchingRegex(#type)"() {
    when:
      def stringValue = matchingRegex(regex)
    then:
      stringValue instanceof RegexStringValue
      (stringValue as RegexStringValue).regex.pattern() == regexAsString
    where:
      type      | regex | regexAsString
      'string'  | '.*'  | regex
      'pattern' | ~/.*/ | regex.pattern()
  }

  def "anyValue()"() {
    when:
      def stringValue = anyValue()
    then:
      stringValue instanceof RegexStringValue
      (stringValue as RegexStringValue).regex.pattern() == '.*'
  }

  def "not()"() {
    given:
      def stringValue = new StringValue() {}
    when:
      def negated = stringValue.not()
    then:
      negated instanceof NegatedStringValue
      (negated as NegatedStringValue).wrapped.is(stringValue)
  }
}
