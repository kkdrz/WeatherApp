package com.tieto.wro.java.a17.wunderground;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.tieto.wro.java.a17.wunderground.model.Response;

public class App 
{
    public static void main( String[] args )
    {
    	//just for test
    	Client client = Client.create();

		WebResource webResource = client
		   .resource("http://api.wunderground.com/api/b6bfc129d8a2c4ea/conditions/q/Poland/Wroclaw.xml");
			//	.resource("http://api.wunderground.com/api/b6bfc129d8a2c4ea/conditions/q/Pila.xml");
		ClientResponse clientResponse = webResource.accept("application/xml")
                   .get(ClientResponse.class);
		Response response = clientResponse.getEntity(Response.class);
		
		System.out.println(response.getCurrentObservation());
    }
}
