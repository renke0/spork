package spork.test

import static java.util.Collections.shuffle

import groovy.json.JsonSlurper
import spock.lang.Specification

class SporkSpecification extends Specification {
  static final LOWERCASE = ('a'..'z').join()
  static final UPPERCASE = ('A'..'Z').join()
  static final DIGITS = ('0'..'9').join()
  static final LETTERS = LOWERCASE + UPPERCASE
  static final ALPHANUMERIC = LETTERS + DIGITS

  static randomString(int length = 5, String fromSample = ALPHANUMERIC) {
    return new Random()
        .with {
          (1..length)
              .collect { fromSample[nextInt(fromSample.length())] }
              .join()
        }
  }

  static randomLowercaseString(int length = 5) {
    return randomString(length, LOWERCASE)
  }

  static <T> T randomItem(List<T> list) {
    def mutable = new ArrayList(list)
    shuffle(mutable)
    return mutable.first()
  }

  static <T> T randomItem(T[] items) {
    return randomItem(items.toList())
  }

  static Map json(String jsonText) {
    new JsonSlurper().parseText(jsonText) as Map
  }
}
