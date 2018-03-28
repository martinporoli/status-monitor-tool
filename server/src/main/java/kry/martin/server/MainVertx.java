package kry.martin.server;

import java.io.IOException;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

/**
 * Main class of the Service Monitoring Tool.
 * 
 * Starts a vertx and deploys the Verticles needed:
 *   - StatusChecker
 *   - MonitorServer
 *   - StatusUpdater
 */
public class MainVertx {

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		DeploymentOptions options;
		try {
			options = new DeploymentOptions(ConfigParser.parse());
		} catch (IOException e) {
			System.out.println("[WARNING] Could not find config file. Using default configuration");
			options = new DeploymentOptions();
		}
		JsonServiceStore store = new JsonServiceStore();
		vertx.deployVerticle(new StatusChecker(), options);
		vertx.deployVerticle(new MonitorServer(store), options);
		vertx.deployVerticle(new StatusUpdater(store), options);
	}
	
}
