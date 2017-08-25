package com.tieto.wro.java.a17.nioapp;

import io.vertx.core.AbstractVerticle;

public class MyVerticle extends AbstractVerticle {

	@Override
	public void stop() throws Exception {
		System.out.println("Weszlo");
		super.stop();
	}

	@Override
	public void start() throws Exception {
		System.out.println("Wyszlo");
		super.start();
	}

}
