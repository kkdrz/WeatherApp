package com.tieto.wro.java.a17.weather.service;

import com.tieto.wro.java.a17.weather.SupportedCitiesProvider;
import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.weather.provider.client.WundergroundClient;
import com.tieto.wro.java.a17.weather.provider.database.DbCache;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.NotFoundException;
import lombok.extern.log4j.Log4j;

@Log4j
public class WeatherServiceCache implements WeatherService {
	
	private final DbCache cache;
	private final WundergroundClient client;
	private final SupportedCitiesProvider suppCitiesProvider;
	
	public WeatherServiceCache(WundergroundClient client, SupportedCitiesProvider supportedCitiesProvider, DbCache cache) {
		this.cache = cache;
		this.suppCitiesProvider = supportedCitiesProvider;
		this.client = client;
		updateCache();
	}
	
	@Override
	public CityWeather getCityWeather(String cityName) throws NotFoundException, IllegalArgumentException {
		City city = suppCitiesProvider.getCityIfSupported(cityName);
		return getCityWeather(city);
	}
	
	@Override
	public CityWeather getCityWeather(City city) throws NotFoundException {
		if (suppCitiesProvider.isSupported(city)) {
			return cache.getCityWeather(city);
		} else throw new NotFoundException();
	}
	
	@Override
	public final void updateCache() {
		List<CityWeather> cityWeathers = getCitiesWeathersFromWeb();
		cityWeathers.forEach((cw) -> {
			cache.saveOrUpdate(cw);
		});
	}
	
	CityWeather getCityWeatherFromWeb(City city) {
		return client.getCityWeather(city);
	}
	
	List<CityWeather> getCitiesWeathersFromWeb() {
		List<CityWeather> citiesWeathers = new ArrayList<>();
		getSupportedCities().forEach((city) -> {
			citiesWeathers.add(getCityWeatherFromWeb(city));
		});
		return citiesWeathers;
	}
	
	@Override
	public SupportedCitiesProvider getSupportedCitiesProvider() {
		return suppCitiesProvider;
	}
	
}
