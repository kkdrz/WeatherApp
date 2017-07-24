package com.tieto.wro.java.a17.wunderground;

import java.util.StringJoiner;

public class WundergroundPathBuilder {

    public String buildPath(String country, String city) {
        StringJoiner url = new StringJoiner("/", "/conditions/q/", ".xml");
        if (!country.equals("")) {
            url.add(country);
        }
        if (!city.equals("")) {
            url.add(city);
        }
        return url.toString();
    }

    public String buildPath(String zmw) {
        StringJoiner url = new StringJoiner("/", "/conditions/q/zmw:", ".xml");
        url.add(zmw);
        return url.toString();
    }
}
