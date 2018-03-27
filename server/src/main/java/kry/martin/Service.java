package kry.martin;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Representation of a service.
 */
public class Service {
	private static final AtomicInteger COUNTER = new AtomicInteger();

	private final int id;
	private final String name;
	private final String url;
	private final Status status;
	private final long lastChecked;
	
	public Service(String name, String url, Status status, long lastChecked) {
		this.id = COUNTER.getAndIncrement();
		this.name = name;
		this.url = url;
		this.status = status;
		this.lastChecked = lastChecked;
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
