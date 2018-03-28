package kry.martin.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class MonitorServerTest {
	
	private Vertx vertx;
	private ServiceStore store;
	
	@Before
	public void setUp(TestContext context) {
		vertx = Vertx.vertx();
		store = new JsonServiceStore("test.json");
		store.createService(new Service("dummy", "https://hej.test", "OK", 1L));
		vertx.deployVerticle(new MonitorServer(store),
				context.asyncAssertSuccess());
	}
	
	@After
	public void tearDown(TestContext context) {
		vertx.close(context.asyncAssertSuccess());
		store.deleteStore();
	}
	
	@Test
	public void testConnection(TestContext context) {
		final Async async = context.async();
		vertx.createHttpClient().getNow(MonitorServer.DEFAULT_PORT, 
				"localhost", "/test",
				response -> {
					response.handler(body -> {
						context.assertTrue(body.toString().contains("This is a test"));
						async.complete();
					});
				});
	}
	
	@Test
	public void testGetAllServices(TestContext context) {
		final Async async = context.async();

		vertx.createHttpClient().getNow(MonitorServer.DEFAULT_PORT, 
				"localhost", "/service",
				response -> {
					response.handler(body -> {
						JsonObject json = new JsonObject(body);
						JsonArray array = json.getJsonArray("services");
						context.assertTrue(array.getJsonObject(0).getString("name").equals("dummy"));
						async.complete();
					});
				});

	}
	
	@Test
	public void testCreateService(TestContext context) {
		Async async = context.async();
		final String json = Json.encode(new Service("serv_name", "https://url.yeah","OK",System.currentTimeMillis()));
		vertx.createHttpClient().post(MonitorServer.DEFAULT_PORT, 
				"localhost", "/service")
			.putHeader("content-type", "application/json")
			.putHeader("content-length", Integer.toString(json.length()))
			.handler(response -> {
				context.assertEquals(response.statusCode(), 200);
				context.assertTrue(response.headers().get("content-type").contains("application/json"));
				response.bodyHandler(body -> {
					final Service s = Json.decodeValue(body.toString(), Service.class);
					context.assertEquals(s.getName(), "serv_name");
					context.assertEquals(s.getStatus(), "OK");
					context.assertEquals(s.getUrl(), "https://url.yeah");
					context.assertNotNull(s.getId());
	
					async.complete();
				});
			}).write(json).end();
	}
}
