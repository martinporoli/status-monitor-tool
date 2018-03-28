package kry.martin.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class JsonServiceStore implements ServiceStore {
	
	private static final Path DEFAULT_STORE_PATH = Paths.get("services.json");
	
	private final Path storePath;
	
	public JsonServiceStore() {
		this.storePath = DEFAULT_STORE_PATH;
		initFile();
	}
	
	public JsonServiceStore(String path) {
		this.storePath = Paths.get(path);
		initFile();
	}
	
	private void initFile() {
		if (!Files.exists(storePath)) {
			JsonObject json = new JsonObject();
			json.put("services", new JsonArray());
			writeToStore(json);
		}
	}
	
	private synchronized JsonObject store() {
		String json = "";
		try {
			json = new String(Files.readAllBytes(storePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new JsonObject(json);
	}
	
	private synchronized void writeToStore(JsonObject json) {
		byte[] bytes = json.encodePrettily().getBytes();
		try {
			Files.write(storePath, bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Service> getAllServices() {
		List<Service> services = new ArrayList<>();
		JsonArray a = store().getJsonArray("services");
		for (int i = 0; i < a.size(); i++) {
			JsonObject json = a.getJsonObject(i);
			services.add(Service.fromJson(json));
		}
		return services;
	}

	@Override
	public Service getService(int id) {
		JsonArray array = store().getJsonArray("services");
		for (int i = 0; i < array.size(); i++) {
			JsonObject json = array.getJsonObject(i);
			Service s = Service.fromJson(json);
			if (s.getId() == id)
				return s;
		}
		return null;
	}

	@Override
	public void removeService(int id) {
		JsonArray array = store().getJsonArray("services");
		int removeIndex = findIndex(array, id);
		if (removeIndex != -1) {
			array.remove(removeIndex);
			initStore(array);
		}
	}

	@Override
	public void createService(Service service) {
		JsonArray a = store().getJsonArray("services");
		a.add(service.toJson());
		initStore(a);
	}
	
	private void initStore(JsonArray array) {
		JsonObject store = new JsonObject();
		store.put("services", array);
		writeToStore(store);
	}

	@Override
	public String toString() {
		return store().toString();
	}

	@Override
	public void deleteStore() {
		try {
			Files.deleteIfExists(storePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateStatus(int id, String status) {
		Service service = getService(id);
		removeService(id);
		service.setStatus(status);
		createService(service);
	}
	
	private int findIndex(JsonArray array, int id) {
		int index = -1;
		for (int i = 0; i < array.size(); i++) {
			JsonObject obj = array.getJsonObject(i);
			Service service = Service.fromJson(obj);
			if (service.getId() == id) {
				index = i;
				break;
			}
		}
		return index;
	}
}
