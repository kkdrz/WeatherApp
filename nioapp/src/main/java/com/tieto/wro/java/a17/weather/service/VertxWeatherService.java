package com.tieto.wro.java.a17.weather.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.tieto.wro.java.a17.nioapp.Config;
import com.tieto.wro.java.a17.weather.WundergroundResponseTransformer;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.wunderground.model.Response;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.log4j.Log4j;

@Log4j
public class VertxWeatherService extends AbstractVerticle {

	private EventBus eventBus;
	private WundergroundResponseTransformer transformer;
	private XmlMapper mapper;

	@Override
	public void start() throws Exception {
		initXmlMapper();
		transformer = new WundergroundResponseTransformer();
		eventBus = vertx.eventBus();

		eventBus.consumer(Config.SERVICE_ADDRESS, this::onMessage);

	}

	public void onMessage(Message<String> message) {
		if (!message.headers().contains("action")) {
			log.error("No action header specified for message with headers "
					+ message.headers() + " and body " + message.body());
			return;
		}

		String action = message.headers().get("action");
		switch (action) {
			case "single":
				getCityWeather(message);
				break;
			case "all":
				getAllCitiesWeathers(message);
				break;
		}
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

	private void getCityWeather(Message<String> message) {
		eventBus.send(Config.CLIENT_ADDRESS, message.body(), reply -> {
			vertx.executeBlocking(future -> {
				CityWeather cityWeather = parseXMLtoCityWeather((String) reply.result().body());
				future.complete(Json.encode(cityWeather));
			}, res -> {
				message.reply(res.result());
			});
		});
	}

	private void getAllCitiesWeathers(Message<String> message) {
		List<CityWeather> citiesWeathers = new ArrayList<>();
		JsonArray cities = new JsonArray(message.body());

		CompositeFuture.all(getRequestsFutures(cities, citiesWeathers)).setHandler(ar -> {
			if (ar.succeeded()) {
				message.reply(Json.encode(citiesWeathers));
			} else {
				log.error("\nFetching one of CityWeather failed.\n");
			}
		});

	}

	private List<Future> getRequestsFutures(JsonArray cities, List<CityWeather> cw) {
		List<Future> futures = new ArrayList<>();

		cities.forEach(c -> {
			Future future = Future.future();
			futures.add(future);
			makeRequest(((JsonObject) c).encode(), future, cw);
		});

		return futures;
	}

	private void makeRequest(String city, Future future, List<CityWeather> cw) {
		eventBus.send(Config.CLIENT_ADDRESS, city, reply -> {
			vertx.executeBlocking(futureBlocking -> {
				CityWeather cityWeather = parseXMLtoCityWeather((String) reply.result().body());
				cw.add(cityWeather);
				futureBlocking.complete();
			}, res -> {
				future.complete();
			});

		});
	}
}
