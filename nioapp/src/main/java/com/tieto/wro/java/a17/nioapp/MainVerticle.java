package com.tieto.wro.java.a17.nioapp;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import lombok.extern.log4j.Log4j;

@Log4j
public class MainVerticle extends AbstractVerticle {

	@Override
	public void start() throws Exception {
		DeploymentOptions options = new DeploymentOptions().setConfig(config());
		
		vertx.deployVerticle("com.tieto.wro.java.a17.weather.controller.VertxController", options);
		vertx.deployVerticle("com.tieto.wro.java.a17.weather.service.VertxWeatherService");
		vertx.deployVerticle("com.tieto.wro.java.a17.weather.provider.client.VertxWundergroundClient");
	}

}