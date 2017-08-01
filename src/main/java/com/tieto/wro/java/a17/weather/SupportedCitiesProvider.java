package com.tieto.wro.java.a17.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.tieto.wro.java.a17.weather.model.City;
import java.io.File;
import java.io.IOException;
import java.util.List;
import lombok.extern.log4j.Log4j;

@Log4j
public class SupportedCitiesProvider {

	public List<City> getSupportedCities(String path) {
		try {
			log.info("Reading cities list from cities.json");
			ObjectMapper mapper = new ObjectMapper();
			List<City> cities = mapper.readValue(
					new File(path),
					TypeFactory.defaultInstance().constructCollectionType(List.class, City.class)
			);
			return cities;
		} catch (IOException ex) {
			log.error("Cannot read cities.json file.");
		}
		return null;
	}

}
