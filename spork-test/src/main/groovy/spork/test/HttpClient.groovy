package spork.test

import static groovy.json.JsonOutput.toJson

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

class HttpClient {
  static final GET = 'GET'
  static final POST = 'POST'
  static final PUT = 'PUT'
  static final DELETE = 'DELETE'
  static final PATCH = 'PATCH'

  private static MEDIA_TYPE_JSON = MediaType.parse('application/json')

  static Response request(String method, String url, Map body = null) {
    def requestBody = body ? RequestBody.create(toJson(body), MEDIA_TYPE_JSON) : emptyBody(method)
    return execute(new Request.Builder()
        .url(url)
        .method(method, requestBody)
        .build())
  }

  private static Response execute(Request request) {
    return new OkHttpClient()
        .newCall(request)
        .execute()
  }

  private static emptyBody(String method) {
    switch (method) {
      case GET:
      case DELETE:
        return null
      case PUT:
      case POST:
      case PATCH:
        return RequestBody.create('', null)
      default:
        throw new IllegalArgumentException("Could not determine body for method $method")
    }
  }
}
