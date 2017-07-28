package com.tieto.wro.java.a17.wunderground.client;

import com.tieto.wro.java.a17.wunderground.model.Response;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import static net.jadler.Jadler.*;
import net.jadler.stubbing.server.jdk.JdkStubHttpServer;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WundergroundClientTest {

	private final String PATH = "/conditions/q/";

	private String API_URL;
	private WundergroundClient client;

	@Before
	public void setUp() {
		initJadlerUsing(new JdkStubHttpServer());
		API_URL = "http://localhost:" + port();
		client = new WundergroundClient(ClientBuilder.newClient(), API_URL);
	}

	@After
	public void tearDown() {
		closeJadler();
	}

	private void jadlerRespondWith(String country, String city, String xmlName) throws FileNotFoundException {
		onRequest()
				.havingMethodEqualTo("GET")
				.havingPathEqualTo(PATH + country + "/" + city + ".xml")
				.respond()
				.withContentType(MediaType.APPLICATION_XML)
				.withBody(new FileReader(new File("src/test/resources/" + xmlName + ".xml").getAbsolutePath()));
	}

	@Test
	public void When_ValidPathPolandWroclaw_Expect_CorrectResponseMappingCurrentObservation() throws FileNotFoundException {
		String country = "Poland";
		String city = "Wroclaw";
		jadlerRespondWith(country, city, "query_poland_wroclaw");

		Response response = client.getWeather(country, city);

		assertHasCurrentObservation(response);

	}

	@Test
	public void When_ValidPathPolandPila_Expect_CorrectResponseMappingResults() throws FileNotFoundException {
		String country = "Poland";
		String city = "Pila";
		jadlerRespondWith(country, city, "query_pila");

		Response response = client.getWeather(country, city);

		assertHasResults(response);
	}

	@Test
	public void When_ValidPathNotExistingCity_Expect_CorrectResponseMappingError() throws FileNotFoundException {
		String country = "Poland";
		String city = "NotExistingCity";
		jadlerRespondWith(country, city, "query_not_found");

		Response response = client.getWeather(country, city);

		assertHasError(response);
	}

	@Test
	public void When_InvalidPath_Expect_IncorrectResponse() throws FileNotFoundException {
		String country = "Poland";
		String city = "Wroclaw";
		jadlerRespondWith(country, city, "query_not_found");

		Response response = client.getWeather(country, "WrongCity/ShuldNotFind");

		assertIsIncorrect(response);
	}

	@Test
	public void When_CityAndCountryNotEmpty_Expect_GetUriReturnsCorrectUri() {
		String country = "Poland";
		String city = "Wroclaw";
		String expectedUri = API_URL + "/conditions/q/Poland/Wroclaw.xml";

		String actualUri = client.getUri(country, city);

		assertTrue(actualUri.equals(expectedUri));
	}

	@Test
	public void When_CountryEmpty_Expect_GetUriReturnsCorrectUri() {
		String country = "";
		String city = "Wroclaw";
		String expectedUri = API_URL + "/conditions/q//Wroclaw.xml";

		String actualUri = client.getUri(country, city);

		assertTrue(actualUri.equals(expectedUri));
	}

	@Test(expected = IllegalArgumentException.class)
	public void When_CountryNull_Expect_ThrowsIllegalArgumentException() {
		String country = null;
		String city = "Wroclaw";

		client.getUri(country, city);
	}

	@Test(expected = IllegalArgumentException.class)
	public void When_CityNull_Expect_ThrowsIllegalArgumentException() {
		String country = "Poland";
		String city = null;

		client.getUri(country, city);
	}

	private void assertIsIncorrect(Response response) {
		assertNull(response.getError());
		assertNull(response.getCurrentObservation());
		assertNull(response.getResults());
	}

	private void assertHasError(Response response) {
		assertNotNull(response.getError());
		assertNull(response.getCurrentObservation());
		assertNull(response.getResults());
	}

	private void assertHasResults(Response response) {
		assertNotNull(response.getResults());
		assertNull(response.getCurrentObservation());
		assertNull(response.getError());
	}

	private void assertHasCurrentObservation(Response response) {
		assertNotNull(response.getCurrentObservation());
		assertNull(response.getResults());
		assertNull(response.getError());
	}
}
