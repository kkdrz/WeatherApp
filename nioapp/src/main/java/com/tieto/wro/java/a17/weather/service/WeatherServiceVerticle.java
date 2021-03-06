package com.tieto.wro.java.a17.weather.service;

import com.tieto.wro.java.a17.nioapp.Config;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.serviceproxy.ProxyHelper;

public class WeatherServiceVerticle extends AbstractVerticle {

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		WebClient client = initWebClient();

		WeatherService.create(client, ready -> {
			if (ready.succeeded()) {
				ProxyHelper.registerService(WeatherService.class, vertx, ready.result(), Config.SERVICE_ADDRESS);
				startFuture.complete();
			} else {
				startFuture.fail(ready.cause());
			}
		});
	}

	private WebClient initWebClient() {
		WebClientOptions options = new WebClientOptions()
				.setDefaultHost(Config.API_HOST)
				.setDefaultPort(Config.API_PORT)
				.setLogActivity(true);
		return WebClient.create(vertx, options);
	}
}
