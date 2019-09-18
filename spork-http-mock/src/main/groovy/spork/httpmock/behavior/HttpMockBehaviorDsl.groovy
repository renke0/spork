package spork.httpmock.behavior





import spork.core.behavior.BehaviorAdapter
import spork.core.behavior.BehaviorDsl

class HttpMockBehaviorDsl extends BehaviorDsl {

  static void httpMock(HttpMockRequestBehaviorDsl requestBehavior, HttpMockResponseBehaviorDsl responseBehavior) {
    def behavior = new HttpMockBehavior(requestBehavior.request, responseBehavior.response)
    new HttpMockBehaviorDsl().setup(behavior)
  }

  @Override
  protected Class<? extends BehaviorAdapter> adapterType() {
    return HttpMockAdapter
  }

  def static any_http_request(@DelegatesTo(HttpMockRequestBehaviorDsl) Closure closure) {
    delegate(new HttpMockRequestBehaviorDsl(), closure)
  }

  def static will_return_a_response(@DelegatesTo(HttpMockResponseBehaviorDsl) Closure closure) {
    delegate(new HttpMockResponseBehaviorDsl(), closure)
  }
}
