package com.tieto.wro.java.a17.weather.controller;

import com.tieto.wro.java.a17.weather.SupportedCitiesProvider;
import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.service.VertxWeatherService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.log4j.Log4j;

@Log4j
public class VertxController extends AbstractVerticle {

	private Router router;
	private EventBus eventBus;
	private SupportedCitiesProvider suppCitiesProvider;

	@Override
	public void start() throws Exception {
		initSuppCitiesProvider();
		initRouter();
		eventBus = vertx.eventBus();

		vertx
				.createHttpServer()
				.requestHandler(router::accept)
				.listen(config().getInteger("http.port", 8080));
	}

	private void initRouter() {
		router = Router.router(vertx);
		router.get("/weather").handler(this::getAllCitiesWeathers);
		router.get("/weather/:city").handler(this::getCityWeather);
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

	private void initSuppCitiesProvider() {
		log.info("cities file: " + config().getString("cities.file"));
		suppCitiesProvider = new SupportedCitiesProvider(config().getString("cities.file"));
	}

}
