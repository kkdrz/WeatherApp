package com.tieto.wro.java.a17.weather.service;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;

@ProxyGen
public interface WeatherServiceProxy {

	@Fluent
	WeatherServiceProxy getAllCitiesWeathers(String cities, Handler<AsyncResult<String>> resultHandler);
	
	@Fluent
	WeatherServiceProxy getCityWeather(String city, Handler<AsyncResult<String>> resultHandler);

	static WeatherServiceProxy createProxy(Vertx vertx, String address) {
		return new WeatherServiceProxyVertxEBProxy(vertx, address);
	}

	static WeatherServiceProxy create(WebClient client, Handler<AsyncResult<WeatherServiceProxy>> readyHandler) {
		return new WeatherServiceProxyImpl(client, readyHandler);
	}
}
