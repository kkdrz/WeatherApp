package com.tieto.wro.java.a17.weather.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.tieto.wro.java.a17.nioapp.Config;
import com.tieto.wro.java.a17.weather.WundergroundResponseTransformer;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.wunderground.model.Response;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.rxjava.ext.web.client.WebClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import lombok.extern.log4j.Log4j;
import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

@Log4j
public class WeatherServiceImpl implements WeatherService {

	private final WundergroundResponseTransformer transformer;
	private XmlMapper mapper;
	private final WebClient client;

	public WeatherServiceImpl(WebClient client, Handler<AsyncResult<WeatherService>> readyHandler) {
		initXmlMapper();
		this.client = client;
		this.transformer = new WundergroundResponseTransformer();
		readyHandler.handle(Future.succeededFuture(this));
	}

	@Override
	public WeatherService getCityWeather(String cityId, Handler<AsyncResult<String>> resultHandler) {
		log.info("GetCityWeather: " + cityId);

		parsedToCityWeatherRequest(cityId).subscribe(cw -> {
			log.info("single city return to handler");
			resultHandler.handle(Future.succeededFuture(Json.encode(cw)));
		});

		return this;
	}

	private Single<CityWeather> parsedToCityWeatherRequest(String cityId) {
		log.info("parsedToCityWeatherRequest");
		return getRequest(cityId).flatMap(this::xmlToCityWeather);
	}

	private Single<CityWeather> xmlToCityWeather(String responseXML) {
		log.info("xmltocityWeather");
		return Single.just(responseXML)
				.flatMap(this::xmlToResponse)
				.flatMap(this::responseToCityWeather);
	}

	private Single<Response> xmlToResponse(String responseXML) {
		try {
			log.info("xmlToResponse");
			return Single.just(mapper.readValue(responseXML, Response.class));
		} catch (IOException ex) {
			log.error("Parsing XML to Response failed.", ex);
			return Single.error(ex);
		}
	}

	private Single<CityWeather> responseToCityWeather(Object response) {
		log.info("responseTocityWeather");
		return Single.just(transformer.transform((Response) response));
	}

	@Override
	public WeatherService getAllCitiesWeathers(JsonArray citiesIds, Handler<AsyncResult<String>> resultHandler) {
		log.info("get all cities");
		List<Single<CityWeather>> citiesWeathers = new ArrayList<>();

		citiesIds.forEach(id -> {
			log.info("Add observable CityWeather");
			citiesWeathers.add(parsedToCityWeatherRequest(id.toString()));
		});

		Observable.from(citiesWeathers)
				.flatMap(single -> single.toObservable().observeOn(Schedulers.computation()))
				.toList()
				.subscribe(result -> {
					log.info("Subscribe, when all done");
					resultHandler.handle(Future.succeededFuture(Json.encode(result)));
				});

		return this;
	}

	private Single<String> getRequest(String cityId) {
		return client.get(new Formatter().format(Config.API_PATH, cityId).toString())
				.rxSend()
				.flatMap(a -> {
					log.info("request from client");
					return Single.just(a.bodyAsString());
				});
	}

	private XmlMapper initXmlMapper() {
		mapper = new XmlMapper();
		mapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(mapper.getTypeFactory()));
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}

}
