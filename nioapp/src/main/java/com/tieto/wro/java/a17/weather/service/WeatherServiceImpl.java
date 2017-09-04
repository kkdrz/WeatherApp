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
		log.info("GetCityWeather: " + cityId);
		client.get(new Formatter().format(Config.API_PATH, cityId).toString())
				.send(ar -> {
					log.info("response from wunder suceeded.");
					CityWeather cityWeather = parseXMLtoCityWeather(ar.result().body().toString());
					resultHandler.handle(Future.succeededFuture(Json.encode(cityWeather)));
				});
		return this;
	}

	@Override
	public WeatherService getAllCitiesWeathers(JsonArray citiesIds, Handler<AsyncResult<String>> handler) {
		JsonArray citiesWeathers = new JsonArray();
		List<Future> futures = new ArrayList<>();

		citiesIds.forEach(c -> {
			Future future = Future.future();
			futures.add(future);

			getCityWeather((String) c, reply -> {
				if (reply.succeeded()) {
					citiesWeathers.add(new JsonObject(reply.result()));
					future.complete();
				}
			});
		});

		CompositeFuture.all(futures).setHandler(ar -> {
			if (ar.succeeded()) {
				handler.handle(Future.succeededFuture(citiesWeathers.encode()));
			} else {
				handler.handle(Future.failedFuture(ar.cause()));
			}
		});

		return this;
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
