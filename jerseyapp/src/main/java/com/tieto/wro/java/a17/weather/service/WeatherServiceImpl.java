package com.tieto.wro.java.a17.weather.service;

import com.tieto.wro.java.a17.weather.SupportedCitiesProvider;
import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.weather.provider.client.WundergroundClient;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.NotFoundException;
import lombok.extern.log4j.Log4j;

@Log4j
@Singleton
public class WeatherServiceImpl implements WeatherService {

	@Inject
	private final WundergroundClient client;
	@Inject
	private final SupportedCitiesProvider suppCitiesProvider;

	public WeatherServiceImpl(WundergroundClient client, SupportedCitiesProvider supportedCitiesProvider) {
		this.client = client;
		this.suppCitiesProvider = supportedCitiesProvider;
	}

	@Override
	public CityWeather getCityWeather(String cityName) throws NotFoundException, IllegalArgumentException {
		City city = suppCitiesProvider.getCityIfSupported(cityName);
		return getCityWeather(city);
	}

	@Override
	public CityWeather getCityWeather(City city) throws NotFoundException {
		try {
			if (suppCitiesProvider.isSupported(city)) {
				return client.getCityWeather(city);
			} else {
				throw new NotFoundException();
			}
		} catch (NotFoundException e) {
			log.error("Requested city: " + city + "not found by provider: " + client, e);
			throw e;
		}
	}

	@Override
	public void updateCache() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Cache not supported.");
	}

	@Override
	public SupportedCitiesProvider getSupportedCitiesProvider() {
		return suppCitiesProvider;
	}

}
