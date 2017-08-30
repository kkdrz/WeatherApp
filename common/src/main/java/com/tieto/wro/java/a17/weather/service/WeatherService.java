package com.tieto.wro.java.a17.weather.service;

import com.tieto.wro.java.a17.weather.SupportedCitiesProvider;
import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.NotFoundException;

public interface WeatherService {

	CityWeather getCityWeather(String cityName) throws NotFoundException, IllegalArgumentException;

	CityWeather getCityWeather(City city) throws NotFoundException;

	default List<CityWeather> getCitiesWeathers() {
		List<CityWeather> citiesWeathers = new ArrayList<>();
		for (City c : getSupportedCities()) {
			citiesWeathers.add(getCityWeather(c));
		}
		return citiesWeathers;
	}

	default void updateCache() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Cache not supported.");
	}

	default AbstractCollection<City> getSupportedCities() {
		return getSupportedCitiesProvider().getSupportedCities();
	}

	SupportedCitiesProvider getSupportedCitiesProvider();

}
