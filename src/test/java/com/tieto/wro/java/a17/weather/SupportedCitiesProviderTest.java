package com.tieto.wro.java.a17.weather;

import com.tieto.wro.java.a17.weather.model.City;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SupportedCitiesProviderTest {

	private SupportedCitiesProvider provider;

	@Before
	public void setUp() {
		provider = new SupportedCitiesProvider();
	}

	@Test
	public void When_FileExist_Expect_GetSupportedCitiesListReturnsListOfCities() {
		List<City> cities = provider.getSupportedCitiesList("src/test/resources/cities_objects.json");

		assertNotNull(cities);
		assertTrue(!cities.isEmpty());
	}

	@Test
	public void When_FileExist_Expect_GetSupportedCitiesMapReturnsMapOfCities() {
		Map<String, String> cities = provider.getSupportedCitiesMap("src/test/resources/cities.json");

		assertNotNull(cities);
		assertTrue(!cities.isEmpty());
	}

}
