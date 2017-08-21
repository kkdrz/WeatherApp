package com.tieto.wro.java.a17.weather.service;

import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.weather.provider.client.WundergroundClient;
import com.tieto.wro.java.a17.weather.provider.database.DbCache;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;


public class WeatherServiceCache implements WeatherService {

	@Inject private final DbCache cache;
	@Inject private final WundergroundClient client;
	private final List<City> supportedCities;

	public WeatherServiceCache(DbCache cache, List<City> supportedCities, WundergroundClient client) {
		this.cache = cache;
		this.supportedCities = supportedCities;
		this.client = client;
		updateCache();
	}

	@Override
	public CityWeather getCityWeather(City city) throws IllegalArgumentException {
		return cache.getCityWeather(city);
	}

	@Override
	public final void updateCache() {
		List<CityWeather> cityWeathers = getCitiesWeathersFromWeb();
		cityWeathers.forEach((cw) -> {
			cache.saveOrUpdate(cw);
		});
	}
	
	@Override
	public List<City> getSupportedCities() {
		return supportedCities;
	}

	CityWeather getCityWeatherFromWeb(City city) {
		return client.getCityWeather(city);
	}

	List<CityWeather> getCitiesWeathersFromWeb() {
		List<CityWeather> citiesWeathers = new ArrayList<>();
		supportedCities.forEach((city) -> {
			citiesWeathers.add(getCityWeatherFromWeb(city));
		});
		return citiesWeathers;
	}
}
