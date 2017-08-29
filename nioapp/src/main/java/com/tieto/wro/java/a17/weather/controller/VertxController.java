package com.tieto.wro.java.a17.weather.controller;

import com.tieto.wro.java.a17.weather.SupportedCitiesProvider;
import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.service.VertxWeatherService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.log4j.Log4j;

@Log4j
public class VertxController extends AbstractVerticle {

	private Router router;
	private EventBus eventBus;
	private SupportedCitiesProvider suppCitiesProvider;
	private final String CONFIG_CITIES_FILE = "cities.file";

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		Future<Void> steps = initSuppCitiesProvider().compose(v -> startHttpServer());
		steps.setHandler(startFuture.completer());
		eventBus = vertx.eventBus();
	}

	public void getAllCitiesWeathers(RoutingContext context) {
		context
				.response()
				.putHeader("content-type", "application/json")
				.end("GET ALL CWs");
	}

	public void getCityWeather(RoutingContext context) {
		String cityName = context.request().getParam("city");
		City city = suppCitiesProvider.getCityIfSupported(cityName);

		eventBus.send(VertxWeatherService.SERVICE_ADDRESS, Json.encode(city), reply -> {
			context
					.response()
					.putHeader("content-type", "application/json")
					.end((String) reply.result().body());
		});
	}

	private Future<Void> initSuppCitiesProvider() {
		Future<Void> future = Future.future();
		try {
			suppCitiesProvider = new SupportedCitiesProvider(config().getString(CONFIG_CITIES_FILE));
			future.complete();
		} catch (IOException ex) {
			Logger.getLogger(VertxController.class.getName()).log(Level.SEVERE, null, ex);
			future.fail(ex);
		}
		return future;
	}

	private Future<Void> startHttpServer() {
		Future<Void> future = Future.future();
		initRouter();
		vertx
				.createHttpServer()
				.requestHandler(router::accept)
				.listen(config().getInteger("http.port", 8080), (ar) -> {
					if (ar.succeeded()) {
						log.info("HTTP server running.");
						future.complete();
					} else {
						log.error("Could not start a HTTP server", ar.cause());
						future.fail(ar.cause());
					}
				});
		return future;
	}

	private void initRouter() {
		router = Router.router(vertx);
		router.get("/weather").handler(this::getAllCitiesWeathers);
		router.get("/weather/:city").handler(this::getCityWeather);
	}
}
