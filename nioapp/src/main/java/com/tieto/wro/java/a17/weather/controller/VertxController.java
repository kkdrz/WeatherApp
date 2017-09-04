package com.tieto.wro.java.a17.weather.controller;

import com.tieto.wro.java.a17.nioapp.Config;
import com.tieto.wro.java.a17.weather.SupportedCitiesProvider;
import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.service.WeatherService;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.Future;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.RoutingContext;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j;

@Log4j
public class VertxController extends AbstractVerticle {

	private Router router;
	private SupportedCitiesProvider suppCitiesProvider;
	private WeatherService service;

	@Override
	public void start(io.vertx.core.Future<Void> startFuture) throws Exception {
		Future<Void> steps = initSuppCitiesProvider().compose(v -> startHttpServer());
		steps.setHandler(startFuture.completer());

		service = WeatherService.createProxy(vertx.getDelegate(), Config.SERVICE_ADDRESS);
	}

	public void getAllCitiesWeathers(RoutingContext context) {
		vertx.executeBlocking(future -> getCitiesIds(future), ar -> {
			service.getAllCitiesWeathers((JsonArray) ar.result(), reply -> {
				context
						.response()
						.putHeader("content-type", "application/json")
						.end(reply.result());
			});
		});

	}

	private void getCitiesIds(Future future) {
		future.complete(
				new JsonArray(
						Json.encode(
								suppCitiesProvider.getSupportedCities()
										.stream()
										.map(City::getZmw)
										.collect(Collectors.toList())
						)));
	}

	public void getCityWeather(RoutingContext context) {
		String cityId = getIdOfCity(context.request().getParam("city"));

		service.getCityWeather(cityId, reply -> {
			context
					.response()
					.putHeader("content-type", "application/json")
					.end(reply.result());
		});

	}

	private String getIdOfCity(String cityName) {
		City city = suppCitiesProvider.getCityIfSupported(cityName);
		return city.getZmw();
	}

	private Future<Void> initSuppCitiesProvider() {
		Future<Void> future = Future.future();
		try {
			suppCitiesProvider = new SupportedCitiesProvider(config().getString(Config.CITIES_FILE));
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
				.rxListen(config().getInteger(Config.HTTP_PORT, Config.DEFAULT_HTTP_PORT))
				.subscribe(
						server -> {
							log.info("HTTP server running on port: " + server.actualPort());
							future.complete();
						},
						failure -> {
							log.error("Could not start a HTTP server", failure.getCause());
							future.fail(failure.getCause());
						});

		return future;
	}

	private void initRouter() {
		router = Router.router(vertx);
		router.get("/weather").handler(this::getAllCitiesWeathers);
		router.get("/weather/:city").handler(this::getCityWeather);
	}
}
