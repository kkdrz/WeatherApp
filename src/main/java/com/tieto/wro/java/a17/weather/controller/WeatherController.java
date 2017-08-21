package com.tieto.wro.java.a17.weather.controller;

import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.weather.provider.client.WundergroundClient;
import com.tieto.wro.java.a17.weather.provider.database.DbCache;
import com.tieto.wro.java.a17.weather.service.WeatherService;
import com.tieto.wro.java.a17.weather.service.WeatherServiceCache;
import java.util.List;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Setter
@Getter
public abstract class WeatherController {

	private String API_URL;
	private final List<City> supportedCities;
	private WeatherService service;

	public WeatherController(List<City> supportedCities, String apiUrl) {
		this.supportedCities = supportedCities;
		this.API_URL = apiUrl;
		initWeatherService();
	}

	protected void initWeatherService() {
		WundergroundClient client = new WundergroundClient(API_URL);
		service = new WeatherServiceCache(new DbCache(), supportedCities, client);
	}

	protected City getCityIfSupported(String cityName) {
		cityName = cityName.toLowerCase();
		for (City c : supportedCities) {
			if (cityName.equals(c.getName())) {
				return c;
			}
		}
		throw new IllegalArgumentException("Requested city \"" + cityName + "\" is not supported.");
	}

	protected List<CityWeather> getCitiesWeathers() {
		log.info("getCitiesWeathers request invoked.");
		List<CityWeather> entities = getService().getCitiesWeathers();
		return entities.isEmpty() ? null : entities;
	}

	protected CityWeather getCityWeather(String cityName) {
		log.info("getCityWeather request for city: \"" + cityName + "\" invoked.");
		try {
			City city = getCityIfSupported(cityName.toLowerCase());
			CityWeather entity = getService().getCityWeather(city);
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
}
