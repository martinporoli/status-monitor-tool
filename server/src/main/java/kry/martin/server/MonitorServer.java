package kry.martin.server;

import java.util.List;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 * Server that handles the REST API.
 */
public class MonitorServer extends AbstractMonitorServer {
	
	private ServiceStore store;
	
	public MonitorServer(ServiceStore store) {
		this.store = store;
	}
	
	@HandlingRequest(path="/test")
	private void handleTest(RoutingContext context) {
		context.response().end("<h1>This is a test</h1>");
	}

	@HandlingRequest(path="/service")
	private void handleServiceGet(RoutingContext context) {
		List<Service> services = store.getAllServices();
		
		JsonArray array = new JsonArray();
		for (Service s : services) {
			array.add(s.toJson());
		}
		JsonObject json = new JsonObject();
		json.put("services", array);
		context.response().end(Json.encodePrettily(json));
	}

	@HandlingRequest(path="/service", method=HttpMethod.POST)
	private void handlerServicePost(RoutingContext context) {
		Service created = Json.decodeValue(context.getBodyAsString(), Service.class);
		created.setId(Service.nextId());
		store.createService(created);
		// Send a request for the status checker to check the status of the service
		vertx.eventBus().send(StatusChecker.ADDRESS, created.getUrl(), reply -> {
			if (reply.failed()) {
				//TODO
			} else {
				String status = (String) reply.result().body();
				store.updateStatus(created.getId(), status);
			}
		});
		
		context.response()
			.setStatusCode(200)
			.putHeader("content-type", "application/json; charset=utf-8")
			.end(Json.encode(created));
	}

	@HandlingRequest(path="/service/:id", method=HttpMethod.DELETE)
	private void handleServiceDelete(RoutingContext context) {
		String id = context.request().getParam("id");
		if (id == null) {
			context.response().setStatusCode(400).end();
	    } else {
	    	store.removeService(Integer.parseInt(id));
	    	context.response()
	    		.setStatusCode(200)
	    		.end();
	    }
	}
}
