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

	private ObjectMapper mapper;

	public SupportedCitiesProvider() {
		mapper = new ObjectMapper();
	}

	public List<City> getSupportedCitiesList(String path) {
		List<City> cities = new ArrayList<>();
		try {
			log.info("Reading cities list from " + path);
			cities = mapper.readValue(
					new File(path),
					TypeFactory.defaultInstance().constructCollectionType(List.class, City.class)
			);
		} catch (IOException ex) {
			log.error("Cannot read file: " + path);
		}
		return cities;
	}

	public Map<String, String> getSupportedCitiesMap(String path) {
		Map<String, String> map = new HashMap<>();
		try {
			log.info("Reading cities map from " + path);
			map = mapper.readValue(new File(path), new TypeReference<Map<String, String>>() {
			});
		} catch (IOException ex) {
			log.error("Cannot read file: " + path);
		}
		return map;
	}

}
