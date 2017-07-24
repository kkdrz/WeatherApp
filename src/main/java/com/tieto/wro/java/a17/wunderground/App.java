package com.tieto.wro.java.a17.wunderground;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("")
public class App extends ResourceConfig {

    public App() {
        packages("com.tieto.wro.java.a17.weather.controller");
    }

    private static final String API_KEY = "b6bfc129d8a2c4ea";
    public static final String API_URL = "http://api.wunderground.com/api/" + API_KEY;

}
