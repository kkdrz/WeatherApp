package com.tieto.wro.java.a17.weather.service;

import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.weather.provider.client.WundergroundClient;
import java.util.List;
import javax.inject.Singleton;
import javax.ws.rs.NotFoundException;
import lombok.extern.log4j.Log4j;

@Log4j
@Singleton
public class WeatherServiceImpl implements WeatherService {

	private final WundergroundClient client;
	private final List<City> supportedCities;

	public WeatherServiceImpl(List<City> supportedCities, WundergroundClient client) {
		this.client = client;
		this.supportedCities = supportedCities;
	}

	@Override
	public CityWeather getCityWeather(City city) throws NotFoundException {
		try {
			return client.getCityWeather(city);
		} catch (NotFoundException e) {
			log.error("Requested city: " + city + "not found by provider: " + client, e);
			throw e;
		}
	}

	@Override
	public List<City> getSupportedCities() {
		return supportedCities;
	}

}
