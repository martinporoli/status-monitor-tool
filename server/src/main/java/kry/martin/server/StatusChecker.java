package kry.martin.server;

import io.vertx.core.AbstractVerticle;

public class StatusChecker extends AbstractVerticle {
	
	public static final String ADDRESS = "StatusCheckerAddress";
	

	@Override
	public void start() throws Exception {
		vertx.eventBus().consumer(ADDRESS)
			.handler(message -> {
				String url = (String) message.body();
				vertx.createHttpClient()
					.getNow(url, response -> {
						int code = response.statusCode();
						String newStatus;
						if (code == 200) {
							newStatus = "OK";
						} else {
							newStatus = "FAIL";
						}
						message.reply(newStatus);
					});
			});
	}
}
