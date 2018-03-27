package kry.martin;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.vertx.core.json.JsonObject;

public class ConfigParser {

	public static final String CONFIG_PATH = "src/main/conf/config.json";
	
	public static final JsonObject parse() throws IOException {
		return parse(CONFIG_PATH);
	}
	
	public static final JsonObject parse(String configPath) throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get(configPath));
		return parse(bytes);
	}
	
	public static final JsonObject parse(byte[] jsonBytes) {
		String json = new String(jsonBytes, StandardCharsets.UTF_8);
		return new JsonObject(json);
	}
}
