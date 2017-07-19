package com.tieto.wro.java.a17.wunderground.client;

import com.tieto.wro.java.a17.wunderground.model.Response;
import java.util.Scanner;
import java.util.StringJoiner;


public class WundergroundIO {
    
    private final Scanner scanner;

    public WundergroundIO() {
        scanner = new Scanner(System.in);
    }
    
    public String getCountryFromConsole() {
        System.out.println("Country: ");
        String country = scanner.nextLine();
        return country.trim();
    }
    
    public String getCityFromConsole() {
        System.out.println("City: ");
        String city = scanner.nextLine();
        return city.trim();
    }
    
    public void printWeather(Response.CurrentObservation observation) {
        StringJoiner weatherString = new StringJoiner("\n", "\n" + observation.getDisplayLocation().getCity() + "\n", "");
        weatherString.add("Temperature: " + observation.getTemperatureString());
        weatherString.add("Wind: " + observation.getWindString());
        weatherString.add("Pressure: " + observation.getPressureMb());
        System.out.println(weatherString);
    }
}
