package spork.lang

class Testing {
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
}
