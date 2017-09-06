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
import java.util.Formatter;
import lombok.extern.log4j.Log4j;
import rx.Observable;
import rx.Single;

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
		getRequest(cityId).subscribe(cw -> {
			resultHandler.handle(Future.succeededFuture(Json.encode(cw)));
		}, error -> {
			log.error("Request error.", error.getCause());
			resultHandler.handle(Future.failedFuture(error));
		});
		return this;
	}

	@Override
	public WeatherService getAllCitiesWeathers(JsonArray citiesIds, Handler<AsyncResult<String>> resultHandler) {
		Observable.from(citiesIds)
				.flatMap(id -> {
					return getRequest(id.toString()).toObservable();
				})
				.toList()
				.subscribe(result -> {
					resultHandler.handle(Future.succeededFuture(Json.encode(result)));
				}, error -> {
					log.error("Collecting CitiesWeathers failed", error.getCause());
					resultHandler.handle(Future.failedFuture(error));
				});

		return this;
	}

	private Single<CityWeather> getRequest(String cityId) {
		return client.get(new Formatter().format(Config.API_PATH, cityId).toString())
				.rxSend()
				.flatMap(h -> {
					return xmlToCityWeather(h.bodyAsString());
				});
	}

	private XmlMapper initXmlMapper() {
		mapper = new XmlMapper();
		mapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(mapper.getTypeFactory()));
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}

	private Single<CityWeather> xmlToCityWeather(String responseXML) {
		return Single.just(responseXML)
				.flatMap(this::xmlToResponse)
				.flatMap(this::responseToCityWeather);
	}

	private Single<Response> xmlToResponse(String responseXML) {
		try {
			return Single.just(mapper.readValue(responseXML, Response.class));
		} catch (IOException ex) {
			log.error("Parsing XML to Response failed.", ex);
			return Single.error(ex);
		}
	}

	private Single<CityWeather> responseToCityWeather(Response response) {
		return Single.just(transformer.transform(response));
	}

}
