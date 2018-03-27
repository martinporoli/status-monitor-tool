package kry.martin;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class ConfigParserTest {
	
	private JsonObject json;
	
	@Before
	public void setup(TestContext context) {
		json = new JsonObject();
		json.put("test1", 47);
		json.put("test2", "TEST");
	}
	
	@Test
	public void testConfigParser(TestContext context) {
		byte[] bytes = json.toString().getBytes();
		JsonObject parsed = ConfigParser.parse(bytes);
		context.assertTrue(parsed.getInteger("test1") == 47);
		context.assertTrue(parsed.getString("test2").equals("TEST"));
	}
	
	@Test
	public void testConfigFileExists(TestContext context) {
		String path = ConfigParser.CONFIG_PATH;
		context.assertTrue(Files.exists(Paths.get(path)));
	}
}
