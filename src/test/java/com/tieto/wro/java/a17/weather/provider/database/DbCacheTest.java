package com.tieto.wro.java.a17.weather.provider.database;

import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.wunderground.TestObjectProvider;
import lombok.extern.log4j.Log4j;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

@Log4j
public class DbCacheTest {

	private DbCache dbCache;

	@Before
	public void setUp() {
		dbCache = new DbCache();
	}

	@Test
	public void When_CityWeatherSavedIntoDB_Expect_QueryReturnsCityWeather() {
		String city = "City";
		CityWeather cw = new TestObjectProvider().getCityWeather(city);

		dbCache.saveOrUpdate(cw);
		CityWeather returned = dbCache.query(city);
		
		assertNotNull(returned);
		assertTrue(cw.equals(returned));
	}
	
	@Test
	public void When_CityWeatherNotInDb_Expect_ReturnsNull() {
		String city = "City";
		
		CityWeather returned = dbCache.query(city);
		
		assertNull(returned);
	}

}
