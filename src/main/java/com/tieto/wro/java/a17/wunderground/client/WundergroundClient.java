package com.tieto.wro.java.a17.wunderground.client;

import com.tieto.wro.java.a17.wunderground.model.Response;
import java.util.StringJoiner;
import javax.ws.rs.client.Client;

public class WundergroundClient {

    private static final String API_KEY = "b6bfc129d8a2c4ea";
    private static final String API_URL = "http://api.wunderground.com/api/" + API_KEY + "/conditions/q/";

    private final Client client;

    public WundergroundClient(Client client) {
        this.client = client;
    }

    public Response getWeather(String country, String city) {
        return getResponse(buildURL(country, city));
    }

    private Response getResponse(String URL) {
        return client.target(URL)
                .request()
                .get(Response.class);
    }

    private String buildURL(String country, String city) {
        StringJoiner url = new StringJoiner("/", API_URL, ".xml");
        url.add(country).add(city);
        return url.toString();
    }

}
