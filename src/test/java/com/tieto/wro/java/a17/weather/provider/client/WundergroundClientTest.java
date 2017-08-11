package com.tieto.wro.java.a17.weather.provider.client;

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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WundergroundClientTest {

	private final String PATH = "/conditions/q/";

	private String API_URL;
	private WundergroundClient client;
	@Mock
	private WundergroundResponseTransformer transformer;

	@Before
	public void setUp() {
		initJadlerUsing(new JdkStubHttpServer());
		API_URL = "http://localhost:" + port();
		client = new WundergroundClient(API_URL, ClientBuilder.newClient(), transformer);
	}

	@After
	public void tearDown() {
		closeJadler();
	}

	private void jadlerRespondWith(String zmw, String xmlName) throws FileNotFoundException {
		onRequest()
				.havingMethodEqualTo("GET")
				.havingPathEqualTo(PATH + "zmw:" + zmw + ".xml")
				.respond()
				.withContentType(MediaType.APPLICATION_XML)
				.withBody(new FileReader(new File("src/test/resources/" + xmlName + ".xml").getAbsolutePath()));
	}

	@Test
	public void When_IdCorrect_Expect_GetWeatherByZmwReturnsResponseCO() throws FileNotFoundException {
		String cityId = "12345678.67864";
		jadlerRespondWith(cityId, "query_poland_wroclaw");

		Response response = client.getWeatherByZmw(cityId);

		assertHasCurrentObservation(response);
	}

	@Test
	public void When_IdIncorrect_Expect_GetWeatherByZmwReturnsResponseErr() throws FileNotFoundException {
		String cityId = "12345678.67864";
		jadlerRespondWith(cityId, "query_not_found");

		Response response = client.getWeatherByZmw(cityId);

		assertHasError(response);
	}

	private void assertHasError(Response response) {
		assertNotNull(response.getError());
		assertNull(response.getCurrentObservation());
		assertNull(response.getResults());
	}

	private void assertHasCurrentObservation(Response response) {
		assertNotNull(response.getCurrentObservation());
		assertNull(response.getResults());
		assertNull(response.getError());
	}

	@Test
	public void When_ZmwGiven_Expect_BuildCorrectUrl() {
		String zmw = "12323.12331";
		String expected = API_URL + "/conditions/q/zmw:" + zmw + ".xml";

		String result = client.buildUrl(zmw);

		assertEquals(expected, result);
	}
}
