package com.tieto.wro.java.a17.weather;

import com.tieto.wro.java.a17.weather.model.City;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SupportedCitiesProviderTest {

	private SupportedCitiesProvider provider;

	@Before
	public void setUp() throws IOException {
		provider = new SupportedCitiesProvider("src/test/resources/cities.json");
	}

	@Test
	public void When_FileExist_Expect_GetSupportedCitiesMapReturnsMapOfCities() throws IOException {
		Map<String, String> cities = provider.getSupportedCitiesMap("src/test/resources/cities.json");

		assertNotNull(cities);
		assertFalse(cities.isEmpty());
	}

	@Test(expected=IOException.class)
	public void When_FileDoesntExist_Expect_ReturnsEmptyMap() throws IOException {
		Map<String, String> cities = provider.getSupportedCitiesMap("wrong/path");

		assertTrue(cities.isEmpty());
	}

	@Test
	public void When_MapIsCorrect_Expect_TransformMapToListReturnsListOfCities() throws IOException {
		Map<String, String> map = provider.getSupportedCitiesMap("src/test/resources/cities.json");
		map.put("wroclaw", "00000.7.12424");
		map.put("lodz", "00000.7.234234");
		map.put("khykyh", "01231.213433");

		HashSet<City> list = provider.transformMapToList(map);

		assertNotNull(list);
		assertTrue(!list.isEmpty());
		assertTrue(list.contains(new City("wroclaw", map.get("wroclaw"))));
	}

}
