package com.tieto.wro.java.a17.weather.provider.client;

import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.weather.provider.CityWeatherProvider;
import javax.ws.rs.NotFoundException;


public class VertxWundergroundClient implements CityWeatherProvider {

	@Override
	public CityWeather getCityWeather(City city) throws NotFoundException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
