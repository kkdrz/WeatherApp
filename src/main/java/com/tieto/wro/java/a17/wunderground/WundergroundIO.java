package com.tieto.wro.java.a17.wunderground;

import com.tieto.wro.java.a17.wunderground.model.Response;
import com.tieto.wro.java.a17.wunderground.model.Response.Results.Result;
import java.util.Scanner;
import java.util.StringJoiner;

public class WundergroundIO {

    private final Scanner scanner;
    private final String INCORRECT_MESSAGE = "Error has occured.";
    private final String ERROR_MESSAGE = "Check if you gave correct city/country name.";

    public WundergroundIO() {
        scanner = new Scanner(System.in);
    }

    public String getCountryFromConsole() {
        System.out.println("Country: ");
        String country = scanner.nextLine();
        return country.replaceAll("\\s+","");
    }

    public String getCityFromConsole() {
        System.out.println("City: ");
        String city = scanner.nextLine();
        return city.replaceAll("\\s+","");
    }

    public void printIncorrect() {
        System.out.println(INCORRECT_MESSAGE);
    }

    public void printError() {
        System.out.println(ERROR_MESSAGE);
    }

    public void printWeather(Response.CurrentObservation observation) {
        StringJoiner weatherString = new StringJoiner("\n", "\n" + observation.getDisplayLocation().getCity() + "\n", "");
        weatherString.add("Temperature: " + observation.getTemperatureString());
        weatherString.add("Wind: " + observation.getWindString());
        weatherString.add("Pressure: " + observation.getPressureMb());
        System.out.println(weatherString);
    }

    public void printResults(Response.Results results) {
        int i = 1;
        System.out.format("%15s %15s %15s %6s\n", "Number", "City", "Country", "State");
        for (Result result : results.getResult()) {
            System.out.format("%15s %15s %15s %6s\n", i++, result.getCity(), result.getCountryName(), result.getState());
        }
    }

    public Response.Results.Result getResultFromConsole(Response.Results results) {
        System.out.println("Choice: ");
        int choice = scanner.nextInt();
        return results.getResult().get(choice - 1);
    }
}
