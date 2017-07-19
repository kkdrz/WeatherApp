package com.tieto.wro.java.a17.wunderground;

import com.tieto.wro.java.a17.wunderground.client.WundergroundClient;
import javax.ws.rs.client.ClientBuilder;

public class App 
{
    public static void main( String[] args )
    {
        WundergroundClient client = new WundergroundClient(ClientBuilder.newClient());
        client.getWeather("Poland", "Pila");
    }
}
