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

    private final String PATH = "/conditions/q/Poland/Wroclaw.xml";

    @Before
    public void setUp() {
        initJadlerUsing(new JdkStubHttpServer());
    }

    @After
    public void tearDown() {
        closeJadler();
    }

    private void jadlerRespondWith(String xmlName) throws FileNotFoundException {
        onRequest()
                .havingMethodEqualTo("GET")
                .respond()
                .withContentType(MediaType.APPLICATION_XML)
                .withBody(new FileReader(new File("src/main/resources/" + xmlName + ".xml").getAbsolutePath()));
    }

    private Response makeRequest() {
        return new WundergroundClient(ClientBuilder.newClient(), "http://localhost:" + port()).getWeather(PATH);
    }

    @Test
    public void When_ValidPathPolandWroclaw_Expect_CorrectResponseMappingCurrentObservation() throws FileNotFoundException {
        jadlerRespondWith("query_poland_wroclaw");

        Response response = makeRequest();

        assertNotNull(response.getCurrentObservation());
        assertNull(response.getResults());
        assertNull(response.getError());

    }

    @Test
    public void When_ValidPathPolandPila_Expect_CorrectResponseMappingResults() throws FileNotFoundException {
        jadlerRespondWith("query_pila");

        Response response = makeRequest();

        assertNotNull(response.getResults());
        assertNull(response.getCurrentObservation());
        assertNull(response.getError());
    }

    @Test
    public void When_ValidPathRandomLetters_Expect_CorrectResponseMappingError() throws FileNotFoundException {
        jadlerRespondWith("query_not_found");

        Response response = makeRequest();

        assertNotNull(response.getError());
        assertNull(response.getCurrentObservation());
        assertNull(response.getResults());
    }

}
