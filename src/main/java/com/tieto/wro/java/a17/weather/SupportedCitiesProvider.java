package com.tieto.wro.java.a17.weather;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.tieto.wro.java.a17.weather.model.City;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j;

@Log4j
public class SupportedCitiesProvider {

	private final ObjectMapper mapper;

	public SupportedCitiesProvider() {
		mapper = new ObjectMapper();
	}

	public List<City> getSupportedCities(String path) {
		List<City> cities = transformMapToList(getSupportedCitiesMap(path));
		log.info("Reading file: " + path + " completed.");
		return cities;
	}

	List<City> getSupportedCitiesList(String path) {
		List<City> cities = new ArrayList<>();
		try {
			log.info("Reading cities list from " + path);
			cities = mapper.readValue(
					new File(path),
					TypeFactory.defaultInstance().constructCollectionType(List.class, City.class)
			);
		} catch (IOException ex) {
			log.error("Cannot read file: " + path + "\n" + ex);
		}
		return cities;
	}

	Map<String, String> getSupportedCitiesMap(String path) {
		Map<String, String> map = new HashMap<>();
		try {
			log.info("Reading cities map from " + path);
			map = mapper.readValue(new File(path), new TypeReference<Map<String, String>>() {
			});
		} catch (IOException ex) {
			log.error("Cannot read file: " + path + "\n" + ex);
		}
		return map;
	}

	List<City> transformMapToList(Map<String, String> citiesMap) {
		List<City> citiesList = new ArrayList<>();

		citiesMap.entrySet().forEach((entry) -> {
			citiesList.add(new City(entry.getKey(), entry.getValue()));
		});

		return citiesList;
	}

}
