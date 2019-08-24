package spork.internal

class TestExecutor {
  private static TestExecutor singleton

  static TestExecutor testExecutor() {
    if (!singleton) {
      singleton = new TestExecutor()
    }
    return singleton
  }
}
