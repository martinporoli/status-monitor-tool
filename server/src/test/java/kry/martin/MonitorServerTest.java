package kry.martin;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class MonitorServerTest {
	
	private Vertx vertx;
	
	@Before
	public void setUp(TestContext context) {
		vertx = Vertx.vertx();
		vertx.deployVerticle(MonitorServer.class.getName(),
				context.asyncAssertSuccess());
	}
	
	@After
	public void tearDown(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
	}
	
	@Test
	public void testMonitorServer(TestContext context) {
		final Async async = context.async();
	
		vertx.createHttpClient().getNow(MonitorServer.DEFAULT_PORT, 
				"localhost", "/",
				response -> {
					response.handler(body -> {
						context.assertTrue(body.toString().contains("HELLO"));
						async.complete();
					});
				});
	}
}
