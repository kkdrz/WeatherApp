package com.tieto.wro.java.a17.wunderground.client;

import com.tieto.wro.java.a17.wunderground.model.Response;
import java.util.StringJoiner;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;

public class WundergroundClient {

    private final String API_URL;
    private final Client client;

    public WundergroundClient(Client client, String apiUrl) {
        this.client = client;
        this.API_URL = apiUrl;
    }

    public Response getWeather(String country, String city) {
        return getResponse(buildURL(country, city));
    }

    public Response getWeather(String zmw) {
        return getResponse(buildURL(zmw));
    }

    private Response getResponse(String URL) {
        return client.target(URL)
                .request(MediaType.APPLICATION_XML)
                .get(Response.class);
    }

    private String buildURL(String country, String city) {
        StringJoiner url = new StringJoiner("/", API_URL + "/conditions/q/", ".xml");
        url.add(country).add(city);
        return url.toString();
    }

    private String buildURL(String zmw) {
        StringJoiner url = new StringJoiner("/", API_URL + "/conditions/q/zmw:", ".xml");
        url.add(zmw);
        return url.toString();
    }

}
