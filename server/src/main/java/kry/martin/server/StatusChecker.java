package kry.martin.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.client.WebClient;

/**
 * Verticle that listens to url-messages on the event bus and
 * performs a check on the given url's status by performing a GET.
 * 
 * Expects the incoming messages to be of type String and that it contains
 * an url.
 * 
 * Replies with a message of type String containing the status code of the
 * GET request.
 */
public class StatusChecker extends AbstractVerticle {
	
	public static final String ADDRESS = "StatusCheckerAddress";
	
	private WebClient client;

	@Override
	public void start() throws Exception {
		client = WebClient.create(vertx);
		vertx.eventBus().consumer(ADDRESS)
			.handler(message -> {
				String url = (String) message.body();
				String host = getHost(url);
				String uri = getUri(url);
				client.get(host, uri).send(ar -> {
					String updatedStatus = "FAIL";
					if (ar.succeeded()) {
						int code = ar.result().statusCode();
						if (code == 200) {
							updatedStatus = "OK";
						}
					} else {
						System.out.println("ERROR: "+ar.cause().getMessage());
					}
					message.reply(updatedStatus);
				});
			});
	}
	
	/**
	 * Parse the host from the url
	 */
	private String getHost(String url) {
		String host = url;
		if (host.contains("://")) {
			host = host.split("://")[1];
		}
		if (host.contains("/")) {
			host = host.split("/")[0];
		}
		return host;
	}
	
	/**
	 * Parses the URI from the url
	 */
	private String getUri(String url) {
		String uri = "";
		if (url.contains("://"))
			url = url.split("://")[1];
		if (url.contains("/")) {
			String[] uris = url.split("/");
			uri = "/";
			for(int i = 1; i < uris.length; i++) {
				uri += uris[i] + "/";
			}
		}
		return uri;
	}
}
