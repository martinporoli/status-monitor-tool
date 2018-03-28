package kry.martin.server;

import java.util.concurrent.atomic.AtomicInteger;

import io.vertx.core.json.JsonObject;

/**
 * Representation of a service.
 */
public class Service {
	private static final AtomicInteger COUNTER = new AtomicInteger();

	private int id;
	private String name;
	private String url;
	private String status;
	private long lastChecked;
	
	public Service(String name, String url, String status, long lastChecked) {
		this.id = COUNTER.getAndIncrement();
		this.name = name;
		this.url = url;
		this.status = status;
		this.lastChecked = lastChecked;
	}
	
	public static Service fromJson(JsonObject json) {
		Service service = new Service();
		service.id = json.getInteger("id");
		service.name = json.getString("name");
		service.url = json.getString("url");
		service.status = json.getString("status");
		service.lastChecked = json.getLong("lastChecked");
		return service;
	}
	
	public Service() {
		this.id = COUNTER.getAndIncrement();
	}
	
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.put("id", id);
		json.put("name", name);
		json.put("url", url);
		json.put("status", status);
		json.put("lastChecked", lastChecked);
		return json;
	}
	
	public int getId() {
		return id;
	}
	
	public long getLastChecked() {
		return lastChecked;
	}
	
	public String getName() {
		return name;
	}
	
	public String getStatus() {
		return status;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setLastChecked(long lastChecked) {
		this.lastChecked = lastChecked;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return this.toJson().toString();
	}
}
