package com.tieto.wro.java.a17.weather.service;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;

@ProxyGen
public interface WeatherService {

	@Fluent
	WeatherService getAllCitiesWeathers(String cities, Handler<AsyncResult<String>> resultHandler);
	
	@Fluent
	WeatherService getCityWeather(String city, Handler<AsyncResult<String>> resultHandler);

	static WeatherService createProxy(Vertx vertx, String address) {
		return new WeatherServiceVertxEBProxy(vertx, address);
	}

	static WeatherService create(WebClient client, Handler<AsyncResult<WeatherService>> readyHandler) {
		return new WeatherServiceImpl(client, readyHandler);
	}
}
