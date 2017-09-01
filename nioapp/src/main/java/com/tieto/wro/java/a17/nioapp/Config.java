package com.tieto.wro.java.a17.nioapp;


public class Config {
	
	public static final String CITIES_FILE = "cities.file";
	public static final String HTTP_PORT = "http.port";
	public static final int DEFAULT_HTTP_PORT = 8080;
	
//	EventBus addresses
	public static final String SERVICE_ADDRESS = "weather.service";
	public static final String CLIENT_ADDRESS = "wunder.client";
	
//	API config
	public static final String API_HOST = "localhost";
	public static final int API_PORT = 8089;
	public static final String API_PATH = "/api.wunderground.com/api/b6bfc129d8a2c4ea/conditions/q/zmw:%s.xml";

}
