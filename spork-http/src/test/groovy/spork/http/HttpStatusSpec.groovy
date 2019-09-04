package spork.http

import spork.test.SporkSpecification

class HttpStatusSpec extends SporkSpecification {
  def "of()"() {
    given:
      def status = randomItem(HttpStatus.values())
    when:
      def result = HttpStatus.of(status.code)
    then:
      status == result
  }
}
