package spork.httpmock.value

class NamedParameter {
  final StringValue key
  final StringValue value

  NamedParameter(StringValue key, StringValue value) {
    this.key = key
    this.value = value
  }
}
