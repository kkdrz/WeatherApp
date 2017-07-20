package com.tieto.wro.java.a17.wunderground;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

public class WundergroundPathBuilderTest {

    private final WundergroundPathBuilder builder;

    public WundergroundPathBuilderTest() {
        builder = new WundergroundPathBuilder();
    }

    @Test
    public void When_PassCountryCity_Expect_CorrectPath() {
        String result = builder.buildPath("Country", "City");
        assertEquals("/conditions/q/Country/City.xml", result);
    }

    @Test
    public void When_PassJustCity_Expect_CorrectPath() {
        String result = builder.buildPath("", "City");
        assertEquals("/conditions/q/City.xml", result);
    }

    @Test
    public void When_PassJustCountry_Expect_CorrectPath() {
        String result = builder.buildPath("Country", "");
        assertEquals("/conditions/q/Country.xml", result);
    }
    
    @Test
    public void When_PassZMW_Expect_CorrectPath() {
        String result = builder.buildPath("1234");
        assertEquals("/conditions/q/zmw:1234.xml", result);
    }
}
