package com.tieto.wro.java.a17.weather.service;

import com.tieto.wro.java.a17.weather.SupportedCitiesProvider;
import com.tieto.wro.java.a17.weather.model.City;
import com.tieto.wro.java.a17.weather.model.CityWeather;
import com.tieto.wro.java.a17.weather.provider.client.WundergroundClient;
import java.util.HashSet;
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
	@Mock
	SupportedCitiesProvider supportedCitiesProvider;

	private WeatherServiceImpl service;
	private final City NOT_SUPP_CITY = new City("City", "213");
	private final City SUPP_CITY = new City("wroclaw", "00000.7.12424");

	@Before
	public void setUp() {
		service = new WeatherServiceImpl(client, supportedCitiesProvider);
	}

	@Test
	public void When_SupportedCity_Expect_GetCityWeatherReturnsCorrectCW() {
		CityWeather cityWeather = new CityWeather();
		when(client.getCityWeather(SUPP_CITY)).thenReturn(cityWeather);
		when(supportedCitiesProvider.isSupported(SUPP_CITY)).thenReturn(Boolean.TRUE);

		CityWeather cwResult = service.getCityWeather(SUPP_CITY);

		assertNotNull(cwResult);
		assertEquals(cityWeather, cwResult);
	}

	@Test(expected = NotFoundException.class)
	public void When_CityDoesntExist_Expect_ThrowsNotFoundException() {
		when(client.getCityWeather(NOT_SUPP_CITY)).thenThrow(new NotFoundException());
		when(supportedCitiesProvider.isSupported(NOT_SUPP_CITY)).thenReturn(Boolean.FALSE);

		CityWeather cwResult = service.getCityWeather(NOT_SUPP_CITY);

		assertNull(cwResult);
	}

	@Test
	public void When_GetCitiesWeathers_Expect_GetCitiesWeathersReturnsListWithNonNull() {
		CityWeather cityWeather = new CityWeather();
		when(client.getCityWeather(Matchers.any(City.class))).thenReturn(cityWeather);
		when(supportedCitiesProvider.getSupportedCities()).thenReturn(getSupportedCities());
		when(supportedCitiesProvider.isSupported(Matchers.any(City.class))).thenReturn(Boolean.TRUE);

		List<CityWeather> cwResult = service.getCitiesWeathers();

		assertFalse(cwResult.contains(null));
		assertFalse(cwResult.isEmpty());
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

	private HashSet<City> getSupportedCities() {
		HashSet<City> cities = new HashSet<>();
		cities.add(new City("wroclaw", "00000.7.12424"));
		cities.add(new City("lodz", "00000.102.12465"));
		cities.add(new City("czestochowa", "00000.484.12550"));
		cities.add(new City("bielsko-biala", "00000.70.12600"));
		cities.add(new City("ulkokalla", "00000.30.02907"));
		return cities;
	}

}
