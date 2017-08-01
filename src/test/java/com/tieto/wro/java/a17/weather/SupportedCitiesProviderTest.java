package com.tieto.wro.java.a17.weather;

import com.tieto.wro.java.a17.weather.model.City;
import java.util.List;
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
	public void When_FileExist_Expect_GetSupportedCitiesReturnsListOfCities() {
		List<City> cities = provider.getSupportedCities("src/test/resources/cities_objects.json");
		
		assertNotNull(cities);
		assertTrue(!cities.isEmpty());
	}
	
}
