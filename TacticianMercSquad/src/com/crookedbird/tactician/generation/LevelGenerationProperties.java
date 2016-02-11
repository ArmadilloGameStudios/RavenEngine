package com.crookedbird.tactician.generation;

import java.util.Random;

public class LevelGenerationProperties {
	private Random rnd;
	
	private int width = 32;
	private int height = 32;
	
	public LevelGenerationProperties() {
		rnd = new Random();
	}
	
	public LevelGenerationProperties(int seed) {
		rnd = new Random(seed);
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}
	
	public Random getRandom() {
		return rnd;
	}
}
