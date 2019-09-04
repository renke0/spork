package spork.test

import static java.util.Collections.shuffle

import spock.lang.Specification

class SporkSpecification extends Specification {
  static final LOWERCASE = ('a'..'z').join()
  static final UPPERCASE = ('A'..'Z').join()
  static final DIGITS = ('0'..'9').join()
  static final LETTERS = LOWERCASE + UPPERCASE
  static final ALPHANUMERIC = LETTERS + DIGITS

  static randomString(int length = 5) {
    return new Random()
      .with {
        (1..length)
          .collect { ALPHANUMERIC[nextInt(ALPHANUMERIC.length())] }
          .join()
      }
  }

  static <T> T randomItem(List<T> list) {
    def mutable = new ArrayList(list)
    shuffle(mutable)
    return mutable.first()
  }

  static <T> T randomItem(T[] items) {
    return randomItem(items.toList())
  }
}
