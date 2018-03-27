package kry.martin;

import java.io.IOException;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

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
		vertx.deployVerticle(new MonitorServer(), options);
	}
	
}
