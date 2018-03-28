package kry.martin.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * The server for the service monitor tool.
 */
public abstract class AbstractMonitorServer extends AbstractVerticle {

	public static final int DEFAULT_PORT = 8882;

	private HttpServer server;

	@Override
	public void start(Future<Void> fut) throws Exception {
		Router router = initRouter();
		router.route().handler(BodyHandler.create());
		server = vertx.createHttpServer();
		server.requestHandler(router::accept);
		server.listen(
				config().getInteger("http.port", DEFAULT_PORT), 
				res -> {
					if (res.succeeded()) {
						fut.complete();
					} else {
						fut.fail(res.cause());
					}
				});
	}

	/**
	 * Initializes the router.
	 * Uses reflection to find the correct method from the derived class.
	 */
	private Router initRouter() {
		Router router = Router.router(vertx);
		// Initialize index page
		router.route("/").handler(StaticHandler.create("assets"));
		for (Method method : this.getClass().getDeclaredMethods()) {
			HandlingRequest hr = method.getAnnotation(HandlingRequest.class);
			if (hr != null) {
				switch (hr.httpMethod()) {
				case GET:
					router.get(hr.path()).handler(routingContext -> {
						handleRequest(method, routingContext);
					});
					break;
				case POST:
					router.route(hr.path()+"*").handler(BodyHandler.create());
					router.post(hr.path()).handler(routingContext -> {
						handleRequest(method, routingContext);
					});
					break;
				case DELETE:
					router.delete(hr.path()).handler(routingContext -> {
						handleRequest(method, routingContext);
					});
					break;
				default:
					break;
				}
			}
		}
		return router;
	}

	/**
	 * Takes a method and a routingContext and invokes the method with the 
	 * context as the only parameter.
	 */
	private void handleRequest(Method method, RoutingContext routingContext) {
		try {
			method.setAccessible(true);
			method.invoke(this, routingContext);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		server.close();
	}
}
