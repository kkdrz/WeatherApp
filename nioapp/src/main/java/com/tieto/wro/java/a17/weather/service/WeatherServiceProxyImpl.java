package com.tieto.wro.java.a17.weather.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.tieto.wro.java.a17.weather.WundergroundResponseTransformer;
import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.wunderground.model.Response;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.log4j.Log4j;

@Log4j
public class WeatherServiceProxyImpl implements WeatherServiceProxy {

	private final WundergroundResponseTransformer transformer;
	private XmlMapper mapper;
	private final WebClient client;

	public WeatherServiceProxyImpl(WebClient client, Handler<AsyncResult<WeatherServiceProxy>> readyHandler) {
		initXmlMapper();
		this.client = client;
		transformer = new WundergroundResponseTransformer();
		readyHandler.handle(Future.succeededFuture(this));
	}

	@Override
	public WeatherServiceProxy getCityWeather(String cityJson, Handler<AsyncResult<String>> resultHandler) {
		City city = Json.decodeValue(cityJson, City.class);
		client.get("/api.wunderground.com/api/b6bfc129d8a2c4ea/conditions/q/zmw:" + city.getZmw() + ".xml")
				.send(ar -> {
					CityWeather cityWeather = parseXMLtoCityWeather(ar.result().body().toString());
					resultHandler.handle(Future.succeededFuture(Json.encode(cityWeather)));
				});
		return this;
	}

	@Override
	public WeatherServiceProxy getAllCitiesWeathers(String citiesJson, Handler<AsyncResult<String>> resultHandler) {
		JsonArray citiesWeathers = new JsonArray();
		JsonArray cities = new JsonArray(citiesJson);
		log.info("Przed compositem");

		getRequestsFutures(cities, citiesWeathers, reply -> {
			CompositeFuture.all(reply.result()).setHandler(ar -> {
				if (ar.succeeded()) {
					log.info("composite succeed");
					resultHandler.handle(Future.succeededFuture(Json.encode(citiesWeathers)));
				} else {
					log.info("composite failed");
					Future.failedFuture(ar.cause());
				}
			});
		});

		log.info("Po composicie");
		return this;
	}

	private void getRequestsFutures(JsonArray cities, JsonArray citiesWeathers, Handler<AsyncResult<List<Future>>> handler) {
		List<Future> futures = new ArrayList<>();

		cities.forEach(c -> {
			Future future = Future.future();
			futures.add(future);
			log.info("makeRequest");
			makeRequest(((JsonObject) c).encode(), reply -> {
				citiesWeathers.add(new JsonObject(reply.result()));
				future.complete();
			});
		});
		log.info("tedt");

		handler.handle(Future.succeededFuture(futures));
	}

	private void makeRequest(String cityJson, Handler<AsyncResult<String>> handler) {
		getCityWeather(cityJson, reply -> {
			if (reply.succeeded()) {
				log.info("make request succeeded");
				handler.handle(Future.succeededFuture(reply.result()));
			} else {
				handler.handle(Future.failedFuture(reply.cause()));
			}
		});
	}

	private CityWeather parseXMLtoCityWeather(String responseXML) {
		try {
			Response response = mapper.readValue(responseXML, Response.class);
			return transformer.transform(response);
		} catch (IOException ex) {
			Logger.getLogger(VertxWeatherService.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}

	private XmlMapper initXmlMapper() {
		mapper = new XmlMapper();
		mapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(mapper.getTypeFactory()));
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}
}
