package spork.httpmock.value

trait Negated<T> {
  abstract T getWrapped()
}
