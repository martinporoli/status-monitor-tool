package kry.martin.server;

import java.util.List;

/**
 * Interface for storing services
 */
public interface ServiceStore {
	List<Service> getAllServices();
	Service getService(int id);
	void removeService(int id);
	void createService(Service service);
	void deleteStore();
	void updateStatus(int id, String status);
}
