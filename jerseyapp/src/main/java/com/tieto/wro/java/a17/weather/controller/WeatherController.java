package com.tieto.wro.java.a17.weather.controller;

import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.weather.service.WeatherService;
import java.util.AbstractCollection;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Setter
@Getter
public abstract class WeatherController {

	@Inject private WeatherService service;
	private String API_URL;

	public WeatherController(String apiUrl) {
		this.API_URL = apiUrl;
	}

	protected List<CityWeather> getCitiesWeathers() {
		log.info("getCitiesWeathers request invoked.");
		List<CityWeather> entities = getService().getCitiesWeathers();
		return entities.isEmpty() ? null : entities;
	}

	protected CityWeather getCityWeather(String cityName) {
		log.info("getCityWeather request for city: \"" + cityName + "\" invoked.");
		try {
			CityWeather entity = getService().getCityWeather(cityName);
			return entity;
		} catch (NotFoundException | IllegalArgumentException e) {
			log.warn("CityWeather for city: \"" + cityName + "\" couldn't be found.", e);
			return null;
		}
	}

	protected Response updateCache() {
		try {
			log.info("Update cache request invoked.");
			getService().updateCache();
			return null;
		} catch (UnsupportedOperationException e) {
			log.error("Cache is not enabled.", e);
			return responseNoCache();
		}
	}

	private Response responseNoCache() {
		return Response.status(Response.Status.NOT_IMPLEMENTED)
				.entity("Cache is not enabled.")
				.build();
	}
	
	public AbstractCollection<City> getSupportedCities() {
		return getService().getSupportedCities();
	}
}
