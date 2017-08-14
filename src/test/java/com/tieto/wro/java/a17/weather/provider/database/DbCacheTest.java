package com.tieto.wro.java.a17.weather.provider.database;

import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.wunderground.TestObjectProvider;
import javax.ws.rs.NotFoundException;
import lombok.extern.log4j.Log4j;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
@Log4j
public class DbCacheTest {

	private DbCache dbCache;
	private final String city = "City";

	@Before
	public void setUp() {
		dbCache = new DbCache();
	}


	@Test
	public void When_CityWeatherSavedIntoDB_Expect_QueryReturnsCityWeather() {
		CityWeather cw = new TestObjectProvider().buildCityWeather(city);

		dbCache.saveOrUpdate(cw);
		CityWeather returned = dbCache.query(city);

		assertNotNull(returned);
		assertTrue(cw.equals(returned));
	}


	@Test(expected = NotFoundException.class)
	public void When_CityWeatherNotInDb_Expect_ThrowNotFoundException() {
		CityWeather returned = dbCache.query(city);

		assertNull(returned);
	}

}
