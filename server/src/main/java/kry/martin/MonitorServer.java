package kry.martin;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

/**
 * The server for the service monitor tool.
 */
public class MonitorServer extends AbstractVerticle {
	
	public static final int DEFAULT_PORT = 8880;

	private HttpServer server;
	
	@Override
	public void start(Future<Void> fut) throws Exception {
		Router router = Router.router(vertx);
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
	
	@Override
	public void stop() throws Exception {
		super.stop();
		server.close();
	}
}
