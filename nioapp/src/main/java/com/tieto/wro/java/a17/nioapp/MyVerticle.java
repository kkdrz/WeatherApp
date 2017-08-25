package com.tieto.wro.java.a17.nioapp;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class MyVerticle extends AbstractVerticle {

	@Override
	public void start() throws Exception {

		Router router = Router.router(vertx);

		router.get("/weather").handler(this::getAllCitiesWeathers);
		router.get("/weather/:city").handler(this::getCityWeather);

		vertx.createHttpServer().requestHandler(router::accept).listen(config().getInteger("http.port"));
	}

	public void getAllCitiesWeathers(RoutingContext context) {
		context
				.response()
				.putHeader("content-type", "application/json")
				.end("GET ALL CWs");
	}

	public void getCityWeather(RoutingContext context) {
		context
				.response()
				.putHeader("content-type", "application/json")
				.end(Json.encodePrettily("GET weather for: " + context.request().getParam("city")));
	}

}
