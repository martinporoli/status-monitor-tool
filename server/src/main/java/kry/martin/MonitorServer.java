package kry.martin;

import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

public class MonitorServer extends AbstractMonitorServer {

	@HandlingRequest(httpMethod=HttpMethod.GET, path="/test")
	private void handleTest(RoutingContext context) {
		context.response().end("<h1>This is a test</h1>");
	}
	
	@HandlingRequest(httpMethod=HttpMethod.GET, path="/service")
	private void handleServiceGet(RoutingContext context) {
		// TODO
	}
	
	@HandlingRequest(httpMethod=HttpMethod.POST, path="/service")
	private void handlerServicePost(RoutingContext context) {
		// TODO
	}
	
	@HandlingRequest(httpMethod=HttpMethod.DELETE, path="/service/:id")
	private void handleServiceDelete(RoutingContext context) {
		// TODO
	}
}
