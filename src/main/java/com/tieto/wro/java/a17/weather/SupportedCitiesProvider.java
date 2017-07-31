package com.tieto.wro.java.a17.weather;

import com.tieto.wro.java.a17.weather.model.City;
import java.util.Arrays;
import java.util.List;


public class SupportedCitiesProvider {

	public List<City> getSupportedCities() {
		List<City> cities = Arrays.asList(
				new City("Wroclaw", "Poland", "00000.7.12424"), 
				new City("Lodz", "Poland", "00000.102.12465"), 
				new City("Czestochowa", "Poland", "00000.484.12550") , 
				new City("Bielsko-Biala", "Poland", "00000.70.12600"), 
				new City("Ulkokalla", "Finland", "00000.30.02907"));
		return cities;
	}


}
