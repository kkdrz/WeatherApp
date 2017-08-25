package com.tieto.wro.java.a17.weather.service;

import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import java.util.List;
import javax.ws.rs.NotFoundException;


public class VertxWeatherServiceImpl implements WeatherService {
	
	private List<City> supportedCities;

	@Override
	public CityWeather getCityWeather(City city) throws NotFoundException {
		System.out.println("ECHO");
		return null;
	}

	@Override
	public List<City> getSupportedCities() {
		return supportedCities;
	}

}
