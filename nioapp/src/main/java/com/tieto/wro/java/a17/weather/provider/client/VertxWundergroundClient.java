package com.tieto.wro.java.a17.weather.provider.client;

import com.tieto.wro.java.a17.weather.model.City;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import lombok.extern.log4j.Log4j;

@Log4j
public class VertxWundergroundClient extends AbstractVerticle {

	public static final String CLIENT_ADDRESS = "wunder.client";
	private WebClient client;
	private EventBus eventBus;

	@Override
	public void start() throws Exception {
		initWebClient();
		eventBus = vertx.eventBus();

		eventBus.consumer(VertxWundergroundClient.CLIENT_ADDRESS, message -> {
			City city = Json.decodeValue((String) message.body(), City.class);
			client.get("/api.wunderground.com/api/b6bfc129d8a2c4ea/conditions/q/zmw:" + city.getZmw() + ".xml")
					.send(ar -> {
						message.reply(ar.result().bodyAsString());
					});
		});
	}

	private void initWebClient() {
		WebClientOptions options = new WebClientOptions()
				.setDefaultHost("localhost")
				.setDefaultPort(8089)
				.setLogActivity(true);
		client = WebClient.create(vertx, options);
	}

}
