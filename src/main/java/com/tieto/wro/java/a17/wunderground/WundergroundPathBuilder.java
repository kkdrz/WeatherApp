package com.tieto.wro.java.a17.wunderground;

import java.util.StringJoiner;
import lombok.extern.log4j.Log4j;

@Log4j
public class WundergroundPathBuilder {

    public String buildPath(String country, String city) {
        log.info("Preparing path for country: " + country + " and city: " + city);
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
        log.info("Preparing path for zmw=" + zmw);
        StringJoiner url = new StringJoiner("/", "/conditions/q/zmw:", ".xml");
        url.add(zmw);
        return url.toString();
    }
}
