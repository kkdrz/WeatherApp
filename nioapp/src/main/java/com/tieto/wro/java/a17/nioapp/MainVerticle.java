package com.tieto.wro.java.a17.nioapp;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import lombok.extern.log4j.Log4j;

@Log4j
public class MainVerticle extends AbstractVerticle {

	@Override
	public void start() throws Exception {

		vertx.deployVerticle("com.tieto.wro.java.a17.weather.controller.VertxController",
				new DeploymentOptions().setConfig(config()).setInstances(4));

		vertx.deployVerticle("com.tieto.wro.java.a17.weather.service.WeatherServiceVerticle",
				new DeploymentOptions().setInstances(4));

		vertx.deployVerticle("com.tieto.wro.java.a17.weather.provider.client.VertxWundergroundClient",
				new DeploymentOptions().setInstances(4));
	}

}
