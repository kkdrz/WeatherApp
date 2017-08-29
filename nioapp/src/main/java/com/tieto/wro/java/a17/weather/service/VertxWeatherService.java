package com.tieto.wro.java.a17.weather.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.tieto.wro.java.a17.weather.WundergroundResponseTransformer;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.weather.provider.client.VertxWundergroundClient;
import com.tieto.wro.java.a17.wunderground.model.Response;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.log4j.Log4j;

@Log4j
public class VertxWeatherService extends AbstractVerticle {

	public static final String SERVICE_ADDRESS = "weather.service";
	private EventBus eventBus;
	private WundergroundResponseTransformer transformer;
	private XmlMapper mapper;

	@Override
	public void start() throws Exception {
		initXmlMapper();
		transformer = new WundergroundResponseTransformer();
		eventBus = vertx.eventBus();

		eventBus.consumer(VertxWeatherService.SERVICE_ADDRESS, message -> {
			eventBus.send(VertxWundergroundClient.CLIENT_ADDRESS, message.body(), reply -> {
				vertx.executeBlocking(future -> {
					CityWeather cityWeather = parseXMLtoCityWeather((String) reply.result().body());
					future.complete(Json.encode(cityWeather));
				}, res -> {
					message.reply(res.result());
				});
			});
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
