//package com.tieto.wro.java.a17.weather.service;
//
//import com.tieto.wro.java.a17.weather.model.City;
//import com.tieto.wro.java.a17.weather.provider.client.WundergroundClient;
//import com.tieto.wro.java.a17.weather.provider.database.DbCache;
//import java.util.Arrays;
//import java.util.List;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import static org.mockito.Mockito.*;
//import org.mockito.runners.MockitoJUnitRunner;
//
//@RunWith(MockitoJUnitRunner.class)
//public class WeatherServiceCacheTest {
//
//	@Mock
//	private WundergroundClient client;
//	@Mock
//	private DbCache cache;
//	private WeatherServiceCache service;
//
//	private final City NOT_SUPP_CITY = new City("City", "213");
//	private final List<City> SUPP_CITIES = getSupportedCities();
//	private final City SUPP_CITY = SUPP_CITIES.get(0);
//
//	@Before
//	public void setUp() {
//		service = new WeatherServiceCache(cache, SUPP_CITIES, client);
//	}
//
//	@Test
//	public void When_ServiceInstantiated_Expect_CacheUpdated() {
//		verify(cache, times(SUPP_CITIES.size())).saveOrUpdate(any());
//	}
//
//	@Test
//	public void When_UpdateCache_Expect_CacheUpdated() {
//		service.updateCache();
//
//		verify(cache, times(SUPP_CITIES.size()*2)).saveOrUpdate(any());
//		//*2 because it is also called on instantiation of service
//	}
//
//	@Test
//	public void When_getCityWeatherCalled_Expect_CacheRespond() {
//		service.getCityWeather(SUPP_CITY);
//		
//		verify(cache, times(1)).getCityWeather(any());
//	}
//
//	@Test
//	public void When_getCitiesWeathersFromWebCalled_Expect_WundergroundClientRespond() {
//		service.getCitiesWeathersFromWeb();
//
//		verify(client, times(SUPP_CITIES.size() * 2)).getCityWeather(any());
//		//*2 because it is also called on instantiation of service
//	}
//
//	private List<City> getSupportedCities() {
//		List<City> cities = Arrays.asList(
//				new City("wroclaw", "00000.7.12424"),
//				new City("lodz", "00000.102.12465"),
//				new City("czestochowa", "00000.484.12550"),
//				new City("bielsko-biala", "00000.70.12600"),
//				new City("ulkokalla", "00000.30.02907"));
//		return cities;
//	}
//
//}
