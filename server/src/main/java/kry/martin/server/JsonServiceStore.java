package kry.martin.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Service store that uses a simple json file as the storage
 */
public class JsonServiceStore implements ServiceStore {
	
	private static final Path DEFAULT_STORE_PATH = Paths.get("services.json");
	
	private final Path storePath;
	
	/**
	 * Constructor which will use a default store path
	 */
	public JsonServiceStore() {
		this.storePath = DEFAULT_STORE_PATH;
		initFile();
	}
	
	/**
	 * Constructor with a specification of the path of the 
	 * json to be stored.
	 */
	public JsonServiceStore(String path) {
		this.storePath = Paths.get(path);
		initFile();
	}
	
	/**
	 * Checks it the file exists. If not, creates it and fill it with an
	 * empty list of services
	 */
	private void initFile() {
		if (!Files.exists(storePath)) {
			JsonObject json = new JsonObject();
			json.put("services", new JsonArray());
			writeToStore(json);
		}
	}
	
	/**
	 * Returns the storage as a JSON object
	 */
	private synchronized JsonObject store() {
		String json = "";
		try {
			json = new String(Files.readAllBytes(storePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new JsonObject(json);
	}
	
	/**
	 * Overwrites the specified JSON to the storage.
	 */
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
			writeArray(array);
		}
	}

	@Override
	public void createService(Service service) {
		JsonArray a = store().getJsonArray("services");
		a.add(service.toJson());
		writeArray(a);
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
		service.setLastChecked(System.currentTimeMillis());
		createService(service);
	}
	
	/**
	 * Since the stored JSON only has one value (list) in the lowest 
	 * level, it is sometimes easier handle the storage as an array.
	 * 
	 * This method writes the array to the correct place in the storage.
	 */
	private void writeArray(JsonArray array) {
		JsonObject store = new JsonObject();
		store.put("services", array);
		writeToStore(store);
	}

	/**
	 * Finds the index of the specified id in the specified array
	 * 
	 * Return -1 if no such index is found.
	 */
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
	
	@Override
	public String toString() {
		return store().toString();
	}
}
