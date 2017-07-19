package com.tieto.wro.java.a17.wunderground;

import com.tieto.wro.java.a17.wunderground.client.WundergroundClient;
import com.tieto.wro.java.a17.wunderground.client.WundergroundIO;
import com.tieto.wro.java.a17.wunderground.model.Response;
import javax.ws.rs.client.ClientBuilder;

public class App 
{
    public static void main( String[] args )
    {
        WundergroundIO console = new WundergroundIO();
        WundergroundClient client = new WundergroundClient(ClientBuilder.newClient());
        Response response = client.getWeather(console.getCountryFromConsole(), console.getCityFromConsole());
        console.printWeather(response.getCurrentObservation());
    }
}
