package com.tieto.wro.java.a17.weather.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.tieto.wro.java.a17.nioapp.Config;
import com.tieto.wro.java.a17.weather.WundergroundResponseTransformer;
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
import java.util.Formatter;
import java.util.List;
import lombok.extern.log4j.Log4j;

@Log4j
public class WeatherServiceImpl implements WeatherService {

	private final WundergroundResponseTransformer transformer;
	private XmlMapper mapper;
	private final WebClient client;

	public WeatherServiceImpl(WebClient client, Handler<AsyncResult<WeatherService>> readyHandler) {
		initXmlMapper();
		this.client = client;
		transformer = new WundergroundResponseTransformer();
		readyHandler.handle(Future.succeededFuture(this));
	}

	@Override
	public WeatherService getCityWeather(String cityId, Handler<AsyncResult<String>> resultHandler) {
		client.get(new Formatter().format(Config.API_PATH, cityId).toString())
				.send(ar -> {
					CityWeather cityWeather = parseXMLtoCityWeather(ar.result().body().toString());
					resultHandler.handle(Future.succeededFuture(Json.encode(cityWeather)));
				});
		return this;
	}

	@Override
	public WeatherService getAllCitiesWeathers(JsonArray citiesIds, Handler<AsyncResult<String>> resultHandler) {
		JsonArray citiesWeathers = new JsonArray();
		log.info("Przed compositem");

		getRequestsFutures(citiesIds, citiesWeathers, reply -> {
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
			makeRequest((String) c, reply -> {
				citiesWeathers.add(new JsonObject(reply.result()));
				future.complete();
			});
		});
		log.info("tedt");

		handler.handle(Future.succeededFuture(futures));
	}

	private void makeRequest(String cityId, Handler<AsyncResult<String>> handler) {
		getCityWeather(cityId, reply -> {
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
			log.error(ex);
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
