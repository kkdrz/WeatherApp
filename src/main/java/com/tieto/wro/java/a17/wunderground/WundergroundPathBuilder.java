package com.tieto.wro.java.a17.wunderground;

import java.util.StringJoiner;


public class WundergroundPathBuilder {
    
    public String buildPath(String country, String city) {
        StringJoiner url = new StringJoiner("/", "/conditions/q/", ".xml");
        url.add(country).add(city);
        return url.toString();
    }

    public String buildPath(String zmw) {
        StringJoiner url = new StringJoiner("/", "/conditions/q/zmw:", ".xml");
        url.add(zmw);
        return url.toString();
    }
}
