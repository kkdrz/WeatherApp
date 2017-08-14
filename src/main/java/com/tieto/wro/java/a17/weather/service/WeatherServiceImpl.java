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
	private List<City> supportedCities;

	public WeatherServiceImpl(List<City> supportedCities, WundergroundClient client) {
		this.client = client;
		initSupportedCities(supportedCities);
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

//	@Override
//	public void updateCache() throws UnsupportedOperationException {
//		throw new UnsupportedOperationException("Cache not supported.");
//	}

	@Override
	public List<City> getSupportedCities() {
		return supportedCities;
	}

	private void initSupportedCities(List<City> supportedCities) {
		if(supportedCities == null || supportedCities.isEmpty()){
			throw new IllegalStateException("List of supported cities cannot be empty/null");
		}
		this.supportedCities = supportedCities;
	}

}
