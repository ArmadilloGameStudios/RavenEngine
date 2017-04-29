package com.crookedbird.engine.graphics3d;

import com.crookedbird.engine.GameEngine;
import com.crookedbird.engine.database.GameData;

public class AnimatedModel {
	private int width, height, length;

	private ModelFrames modelAnim;

	public AnimatedModel(GameData modelData) {
		if (modelData.getData("width") != null
				&& modelData.getData("width").isInteger()) {
			width = modelData.getData("width").getInteger();
		} else {
			width = 16;
		}

		if (modelData.getData("height") != null
				&& modelData.getData("height").isInteger()) {
			height = modelData.getData("height").getInteger();
		} else {
			height = 16;
		}

		if (modelData.getData("length") != null
				&& modelData.getData("length").isInteger()) {
			length = modelData.getData("length").getInteger();
		} else {
			length = 16;
		}

		modelAnim = GameEngine.getEngine().getModelReferenceAsset(
				modelData.getData("src").getString());
	}

	public void draw(String animationstate, long timeOffset) {
		modelAnim.getModel(animationstate, timeOffset).draw();
	}

	public void drawError() {

	}

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}

	public float getLength() {
		return length;
	}

	public int getFrameTime(String animationstate) {
		return modelAnim.getFrameTime(animationstate);
	}
}
