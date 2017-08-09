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
		assertFalse(cities.isEmpty());
	}

	@Test
	public void When_FileExist_Expect_GetSupportedCitiesMapReturnsMapOfCities() {
		Map<String, String> cities = provider.getSupportedCitiesMap("src/test/resources/cities.json");

		assertNotNull(cities);
		assertFalse(cities.isEmpty());
	}
	
	@Test
	public void When_FileDoesntExist_EXpect_ReturnsEmptyMap() {
		Map<String, String> cities = provider.getSupportedCitiesMap("wrong/path");
		
		assertTrue(cities.isEmpty());
	}

	@Test
	public void When_MapIsCorrect_Expect_TransformMapToListReturnsListOfCities() {
		Map<String, String> map = provider.getSupportedCitiesMap("src/test/resources/cities.json");
		map.put("wroclaw", "00000.7.12424");
		map.put("lodz", "00000.7.234234");
		map.put("khykyh", "01231.213433");

		List<City> list = provider.transformMapToList(map);

		assertNotNull(list);
		assertTrue(!list.isEmpty());
		assertTrue(map.get("wroclaw").equals(list.get(0).getZmw()));
	}

}
