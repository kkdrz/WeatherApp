package com.tieto.wro.java.a17.wunderground.model;

import com.tieto.wro.java.a17.wunderground.TestObjectProvider;
import static org.junit.Assert.*;
import org.junit.Test;

public class ResponseTest {

    TestObjectProvider provider;

    public ResponseTest() {
        provider = new TestObjectProvider();
    }

    @Test
    public void When_ValidResponse_Expect_isValidTrueAndReturnsCorrectResultType() {
        Response response = provider.getResponse("currentObservation");
        assertEquals(response.getResponseType(), Response.ResponseType.CURRENT_OBSERVATION);

        response = provider.getResponse("results");
        assertEquals(response.getResponseType(), Response.ResponseType.RESULTS);

        response = provider.getResponse("error");
        assertEquals(response.getResponseType(), Response.ResponseType.ERROR);

        response = provider.getResponse("incorrect");
        assertEquals(response.getResponseType(), Response.ResponseType.INCORRECT);

    }
}
