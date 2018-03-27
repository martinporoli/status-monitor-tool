package kry.martin;

/**
 * Representation of a service.
 */
public class Service {

	private final String id;
	private final String name;
	private final String url;
	private final Status status;
	private final long lastChecked;
	
	public Service(String id, String name, String url, Status status, long lastChecked) {
		this.id = id;
		this.name = name;
		this.url = url;
		this.status = status;
		this.lastChecked = lastChecked;
	}
	
	public String getId() {
		return id;
	}
	
	public long getLastChecked() {
		return lastChecked;
	}
	
	public String getName() {
		return name;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public String getUrl() {
		return url;
	}
	
	private enum Status {
		OK, FAIL;
	}
}
