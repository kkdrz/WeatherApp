package com.tieto.wro.java.a17.weather.service;

import com.tieto.wro.java.a17.weather.SupportedCitiesProvider;
import com.tieto.wro.java.a17.weather.WundergroundResponseTransformer;
import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.wunderground.client.WundergroundClient;
import com.tieto.wro.java.a17.wunderground.model.Response;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j;

@Log4j
public class WeatherServiceImpl {

	private final WundergroundClient client;
	private final WundergroundResponseTransformer transformer;
	private List<City> supportedCities;

	public WeatherServiceImpl(WundergroundClient client) {
		this(client, new WundergroundResponseTransformer());
	}

	public WeatherServiceImpl(WundergroundClient client, WundergroundResponseTransformer transformer) {
		this.client = client;
		this.transformer = transformer;
		SupportedCitiesProvider provider = new SupportedCitiesProvider();
		this.supportedCities = provider.getSupportedCities("src/main/resources/cities.json");
		log.info("WeatherService instantiated.");
	}

	public CityWeather getCityWeather(String cityName) {
		log.info("getCityWeather for \"" + cityName + "\" invoked.");
		City city = findCityByName(cityName.toLowerCase());
		if (city == null) {
			log.warn("Requested city (" + cityName + ") is not supported.");
			return null;
		}
		return getCityWeatherById(city.getId());
	}

	private CityWeather getCityWeatherById(String id) {
		Response response = client.getWeatherById(id);
		if (response == null) {
			log.warn("Response from client is null. Requested zmw: " + id);
			return null;
		}
		return transformer.transform(response);
	}

	private City findCityByName(String cityName) {
		for (City c : supportedCities) {
			if (cityName.equals(c.getName())) {
				return c;
			}
		}
		return null;
	}

	public List<CityWeather> getCitiesWeathers() {
		log.info("getCitiesWeathers method invoked.");
		List<CityWeather> response = new ArrayList<>();
		for (City city : supportedCities) {
			response.add(getCityWeatherById(city.getId()));
		}
		log.info("Returning response: List<CityWeather>");
		return response;
	}

}
