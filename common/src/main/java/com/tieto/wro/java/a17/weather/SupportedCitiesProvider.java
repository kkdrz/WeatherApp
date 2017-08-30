package com.tieto.wro.java.a17.weather;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tieto.wro.java.a17.weather.model.City;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import lombok.extern.log4j.Log4j;

@Log4j
public class SupportedCitiesProvider {

	private final ObjectMapper mapper;
	private HashSet<City> supportedCities;

	public SupportedCitiesProvider(String path) throws IOException {
		mapper = new ObjectMapper();
		loadSupportedCities(path);
	}

	private void loadSupportedCities(String path) throws IOException {
		supportedCities = transformMapToList(getSupportedCitiesMap(path));
	}

	Map<String, String> getSupportedCitiesMap(String path) throws IOException {
		Map<String, String> map = new HashMap<>();
		try {
			log.info("Reading cities map from " + path);
			map = mapper.readValue(new File(path), new TypeReference<Map<String, String>>() {
			});
		} catch (IOException ex) {
			log.error("Cannot read file: " + path + "\n" + ex);
			throw ex;
		}
		return map;
	}

	HashSet<City> transformMapToList(Map<String, String> citiesMap) {
		HashSet<City> citiesList = new HashSet<>();

		citiesMap.entrySet().forEach((entry) -> {
			citiesList.add(new City(entry.getKey(), entry.getValue()));
		});

		return citiesList;
	}
	
	public boolean isSupported(City city) {
		return supportedCities.contains(city);
	}

	public City getCityIfSupported(String cityName) throws IllegalArgumentException {
		cityName = cityName.toLowerCase();
		for (City c : supportedCities) {
			if (cityName.equals(c.getName())) {
				return c;
			}
		}
		throw new IllegalArgumentException("Requested city \"" + cityName + "\" is not supported.");
	}

	public HashSet<City> getSupportedCities() {
		return supportedCities;
	}

	public void setPath(String path) throws IOException {
		loadSupportedCities(path);
	}

}
