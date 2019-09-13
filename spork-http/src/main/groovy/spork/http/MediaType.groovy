package spork.http

class MediaType {
  final String mimeType

  private MediaType(String mimeType) {
    this.mimeType = mimeType
  }

  static MediaType valueOf(String mimeType) {
    return new MediaType(mimeType)
  }

  static final String ALL_VALUE = '*/*'
  static final String APPLICATION_ATOM_XML_VALUE = 'application/atom+xml'
  static final String APPLICATION_FORM_URLENCODED_VALUE = 'application/x-www-form-urlencoded'
  static final String APPLICATION_JSON_VALUE = 'application/json'
  static final String APPLICATION_JSON_UTF8_VALUE = 'application/json;charset=UTF-8'
  static final String APPLICATION_OCTET_STREAM_VALUE = 'application/octet-stream'
  static final String APPLICATION_PDF_VALUE = 'application/pdf'
  static final String APPLICATION_PROBLEM_JSON_VALUE = 'application/problem+json'
  static final String APPLICATION_PROBLEM_JSON_UTF8_VALUE = 'application/problem+json;charset=UTF-8'
  static final String APPLICATION_PROBLEM_XML_VALUE = 'application/problem+xml'
  static final String APPLICATION_RSS_XML_VALUE = 'application/rss+xml'
  static final String APPLICATION_STREAM_JSON_VALUE = 'application/stream+json'
  static final String APPLICATION_XHTML_XML_VALUE = 'application/xhtml+xml'
  static final String APPLICATION_XML_VALUE = 'application/xml'
  static final String IMAGE_GIF_VALUE = 'image/gif'
  static final String IMAGE_JPEG_VALUE = 'image/jpeg'
  static final String IMAGE_PNG_VALUE = 'image/png'
  static final String MULTIPART_FORM_DATA_VALUE = 'multipart/form-data'
  static final String TEXT_EVENT_STREAM_VALUE = 'text/event-stream'
  static final String TEXT_HTML_VALUE = 'text/html'
  static final String TEXT_MARKDOWN_VALUE = 'text/markdown'
  static final String TEXT_PLAIN_VALUE = 'text/plain'
  static final String TEXT_XML_VALUE = 'text/xml'

  static MediaType ALL = valueOf(ALL_VALUE)
  static MediaType APPLICATION_ATOM_XML = valueOf(APPLICATION_ATOM_XML_VALUE)
  static MediaType APPLICATION_FORM_URLENCODED = valueOf(APPLICATION_FORM_URLENCODED_VALUE)
  static MediaType APPLICATION_JSON = valueOf(APPLICATION_JSON_VALUE)
  static MediaType APPLICATION_JSON_UTF8 = valueOf(APPLICATION_JSON_UTF8_VALUE)
  static MediaType APPLICATION_OCTET_STREAM = valueOf(APPLICATION_OCTET_STREAM_VALUE)
  static MediaType APPLICATION_PDF = valueOf(APPLICATION_PDF_VALUE)
  static MediaType APPLICATION_PROBLEM_JSON = valueOf(APPLICATION_PROBLEM_JSON_VALUE)
  static MediaType APPLICATION_PROBLEM_JSON_UTF8 = valueOf(APPLICATION_PROBLEM_JSON_UTF8_VALUE)
  static MediaType APPLICATION_PROBLEM_XML = valueOf(APPLICATION_PROBLEM_XML_VALUE)
  static MediaType APPLICATION_RSS_XML = valueOf(APPLICATION_RSS_XML_VALUE)
  static MediaType APPLICATION_STREAM_JSON = valueOf(APPLICATION_STREAM_JSON_VALUE)
  static MediaType APPLICATION_XHTML_XML = valueOf(APPLICATION_XHTML_XML_VALUE)
  static MediaType APPLICATION_XML = valueOf(APPLICATION_XML_VALUE)
  static MediaType IMAGE_GIF = valueOf(IMAGE_GIF_VALUE)
  static MediaType IMAGE_JPEG = valueOf(IMAGE_JPEG_VALUE)
  static MediaType IMAGE_PNG = valueOf(IMAGE_PNG_VALUE)
  static MediaType MULTIPART_FORM_DATA = valueOf(MULTIPART_FORM_DATA_VALUE)
  static MediaType TEXT_EVENT_STREAM = valueOf(TEXT_EVENT_STREAM_VALUE)
  static MediaType TEXT_HTML = valueOf(TEXT_HTML_VALUE)
  static MediaType TEXT_MARKDOWN = valueOf(TEXT_MARKDOWN_VALUE)
  static MediaType TEXT_PLAIN = valueOf(TEXT_PLAIN_VALUE)
  static MediaType TEXT_XML = valueOf(TEXT_XML_VALUE)
}
