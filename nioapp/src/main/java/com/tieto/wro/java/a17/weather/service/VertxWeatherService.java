package com.tieto.wro.java.a17.weather.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.tieto.wro.java.a17.nioapp.Config;
import com.tieto.wro.java.a17.weather.WundergroundResponseTransformer;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.wunderground.model.Response;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import java.io.IOException;
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
	
	public void onMessage(Message<JsonObject> message) {
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
	
	private void getCityWeather(Message<JsonObject> message) {
		eventBus.send(Config.CLIENT_ADDRESS, message.body(), reply -> {
			vertx.executeBlocking(future -> {
				CityWeather cityWeather = parseXMLtoCityWeather((String) reply.result().body());
				future.complete(Json.encode(cityWeather));
			}, res -> {
				message.reply(res.result());
			});
		});
	}
	
	private void getAllCitiesWeathers(Message<JsonObject> message) {
//		JsonArray cities = new JsonArray(message.body());
//		
//		eventBus.send(VertxWundergroundClient.CLIENT_ADDRESS, message.body(), reply -> {
//			vertx.executeBlocking(future -> {
//				CityWeather cityWeather = parseXMLtoCityWeather((String) reply.result().body());
//				future.complete(Json.encode(cityWeather));
//			}, res -> {
//				message.reply(res.result());
//			});
//		});
		message.reply("GET ALL");
		
		log.info("getAllCitiesW");
	}
}
