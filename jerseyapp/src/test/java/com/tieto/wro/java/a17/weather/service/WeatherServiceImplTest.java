package com.tieto.wro.java.a17.weather.service;

import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.weather.provider.client.WundergroundClient;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.NotFoundException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WeatherServiceImplTest {

	@Mock
	WundergroundClient client;

	private WeatherServiceImpl service;
	private final City NOT_SUPP_CITY = new City("City", "213");
	private final List<City> SUPP_CITIES = getSupportedCities();
	private final City SUPP_CITY = SUPP_CITIES.get(0);

	@Before
	public void setUp() {
		service = new WeatherServiceImpl(SUPP_CITIES, client);
	}

	@Test
	public void When_SupportedCity_Expect_GetCityWeatherReturnsCorrectCW() {
		CityWeather cityWeather = new CityWeather();
		when(client.getCityWeather(SUPP_CITY)).thenReturn(cityWeather);

		CityWeather cwResult = service.getCityWeather(SUPP_CITY);

		assertNotNull(cwResult);
		assertEquals(cityWeather, cwResult);
	}

	@Test(expected = NotFoundException.class)
	public void When_CityDoesntExist_Expect_ThrowsNotFoundException() {
		when(client.getCityWeather(NOT_SUPP_CITY)).thenThrow(new NotFoundException());
		
		CityWeather cwResult = service.getCityWeather(NOT_SUPP_CITY);

		assertNull(cwResult);
	}

	@Test
	public void When_GetCitiesWeathers_Expect_GetCitiesWeathersReturnsListWithNonNull() {
		CityWeather cityWeather = new CityWeather();
		when(client.getCityWeather(Matchers.any(City.class))).thenReturn(cityWeather);

		List<CityWeather> cwResult = service.getCitiesWeathers();

		assertFalse(cwResult.contains(null));
		assertFalse(cwResult.isEmpty());
	}

	@Test(expected = IllegalStateException.class)
	public void When_SupportedCityEmpty_Expect_ThrowsIllegalStateException() {
		WeatherServiceImpl wservice = new WeatherServiceImpl(Collections.emptyList(), client);

		assertNull(wservice);
	}

	@Test(expected = NotFoundException.class)
	public void When_ProviderProblem_Expect_ThrowsNotFoundException() {
		when(client.getCityWeather(SUPP_CITY)).thenThrow(NotFoundException.class);

		CityWeather cwResult = service.getCityWeather(SUPP_CITY);

		assertNull(cwResult);
	}

	
	@Test(expected = UnsupportedOperationException.class)
	public void When_UpdateAndNoCache_Expect_ThrowsUnsupportedOperationException() {
		service.updateCache();
	}

	private List<City> getSupportedCities() {
		List<City> cities = Arrays.asList(
				new City("wroclaw", "00000.7.12424"),
				new City("lodz", "00000.102.12465"),
				new City("czestochowa", "00000.484.12550"),
				new City("bielsko-biala", "00000.70.12600"),
				new City("ulkokalla", "00000.30.02907"));
		return cities;
	}

}
