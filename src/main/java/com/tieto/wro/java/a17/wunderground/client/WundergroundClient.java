package com.tieto.wro.java.a17.wunderground.client;

import com.tieto.wro.java.a17.wunderground.model.Response;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import lombok.extern.log4j.Log4j;

@Log4j
public class WundergroundClient {

    private final String API_URL;
    private final Client client;

    public WundergroundClient(String apiUrl) {
        this(ClientBuilder.newClient(), apiUrl);
    }

    public WundergroundClient(Client client, String apiUrl) {
        this.API_URL = apiUrl;
        this.client = client;
        log.info("WundergroundClient with apiURL=" + apiUrl + " is instantiated.");
    }

    public Response getWeather(String country, String city) {
        String URI = getUri(country, city);
        Response response = getResponse(URI);
        log.info("Response received.");
        return response;
    }

    public Response getWeather(String zmw) {
        String URI = getUri(zmw);
        return getResponse(URI);
    }

    private Response getResponse(String URI) {
        log.info("Getting response from URL: " + URI);
        return client.target(URI)
                .request(MediaType.APPLICATION_XML)
                .get(Response.class);
    }

    private String getUri(String country, String city) {
        return UriBuilder.fromPath(API_URL)
                .path("/conditions/q/{country}/{city}.xml")
                .build(country, city)
                .toString();
    }

    private String getUri(String zmw) {
        return UriBuilder.fromPath(API_URL)
                .path("/conditions/q/zmw:{zmw}.xml")
                .build(zmw)
                .toString();
    }
}
