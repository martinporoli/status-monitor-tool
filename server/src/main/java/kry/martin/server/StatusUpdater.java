package kry.martin.server;

import io.vertx.core.AbstractVerticle;

/**
 * Verticle that polls the status checker and updates the url accordingly.
 */
public class StatusUpdater extends AbstractVerticle {
	
	ServiceStore store;
	
	public StatusUpdater(ServiceStore store) {
		this.store = store;
	}

	@Override
	public void start() throws Exception {
		vertx.setPeriodic(60000, handler -> {
			for (Service service : store.getAllServices()) {
				updateStatus(service);
			}
		});
	}
	
	public void updateStatus(int id) {
		updateStatus(store.getService(id));
	}
	
	/**
	 * Sends a request to the status checker. Uses the reply to update the storage.
	 */
	public void updateStatus(Service service) {
		StatusChecker.checkStatus(vertx.eventBus(), service.getUrl(), reply -> {
			if (reply.failed()) {
				System.err.println("[StatusUpdater] ERROR: Message from Status Checker failed."
						+reply.cause().getMessage());
			} else {
				String status = (String) reply.result().body();
				store.updateStatus(service.getId(), status);
			}
		});
	}
}
