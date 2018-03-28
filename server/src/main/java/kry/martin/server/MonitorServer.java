package kry.martin.server;

import java.util.List;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class MonitorServer extends AbstractMonitorServer {
	
	private ServiceStore store;
	
	public MonitorServer(ServiceStore store) {
		this.store = store;
	}
	
	@HandlingRequest(httpMethod=HttpMethod.GET, path="/test")
	private void handleTest(RoutingContext context) {
		context.response().end("<h1>This is a test</h1>");
	}

	@HandlingRequest(httpMethod=HttpMethod.GET, path="/service")
	private void handleServiceGet(RoutingContext context) {
		System.out.println("GET service/");
		List<Service> services = store.getAllServices();
		
		JsonArray array = new JsonArray();
		for (Service s : services) {
			array.add(s.toJson());
		}
		JsonObject json = new JsonObject();
		json.put("services", array);
		context.response().end(Json.encodePrettily(json));
	}

	@HandlingRequest(httpMethod=HttpMethod.POST, path="/service")
	private void handlerServicePost(RoutingContext context) {
		System.out.println("POST service/");
		Service created = Json.decodeValue(context.getBodyAsString(), Service.class);
		store.createService(created);
		context.response()
			.setStatusCode(200)
			.putHeader("content-type", "application/json; charset=utf-8")
			.end(Json.encode(created));
	}

	@HandlingRequest(httpMethod=HttpMethod.DELETE, path="/service/:id")
	private void handleServiceDelete(RoutingContext context) {
		String id = context.request().getParam("id");
		System.out.println("DELETE service/"+id);
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
