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
		assertEquals(Response.ResponseType.CURRENT_OBSERVATION, response.getResponseType());

		response = provider.getResponse("results");
		assertEquals(Response.ResponseType.RESULTS, response.getResponseType());

		response = provider.getResponse("error");
		assertEquals(Response.ResponseType.ERROR, response.getResponseType());

		response = provider.getResponse("incorrect");
		assertEquals(Response.ResponseType.INCORRECT, response.getResponseType());

	}
}
