package com.tieto.wro.java.a17.weather.database;

import com.tieto.wro.java.a17.weather.provider.database.DbCache;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.wunderground.TestObjectProvider;
import lombok.extern.log4j.Log4j;
import org.junit.Assert;
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
	public void When_CityWeatherInsertedIntoDB_Expect_QueryReturnsCityWeather() {
		CityWeather cw = new TestObjectProvider().getCityWeather();
		
		dbCache.insert(cw);
		
		CityWeather returned = dbCache.query(cw.getLocation());
		Assert.assertTrue(cw.equals(returned));
	}
	
}
