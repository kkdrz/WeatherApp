package com.tieto.wro.java.a17.weather;

import com.tieto.wro.java.a17.weather.model.City;
import java.util.Arrays;
import java.util.List;


public class SupportedCitiesProvider {

	public static List<City> getSupportedCities() {
		List<City> cities = Arrays.asList(
				new City("wroclaw", "poland", "00000.7.12424"), 
				new City("lodz", "poland", "00000.102.12465"), 
				new City("czestochowa", "poland", "00000.484.12550") , 
				new City("bielsko-biala", "poland", "00000.70.12600"), 
				new City("ulkokalla", "finland", "00000.30.02907"));
		return cities;
	}


}
