package com.raven.engine.graphics3d;

import com.raven.engine.GameEngine;
import com.raven.engine.database.GameData;

public class AnimatedModel {
	private int width, height, length;

	private ModelFrames modelFramesAnim;

	public AnimatedModel(ModelData data) {
		width = 16;
		height = 16;
		length = 16;

		modelFramesAnim = data.getModelFrames();

		data.setAnimatedModel(this);
	}

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

		modelFramesAnim = GameEngine.getEngine().getModelReferenceAsset(
				modelData.getData("src").getString());
	}

	void updateModelFrames(ModelData data) {
		modelFramesAnim = data.getModelFrames();
	}

	public void draw(String animationstate, long timeOffset) {
		modelFramesAnim.getModel(animationstate, timeOffset).draw();
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
		return modelFramesAnim.getFrameTime(animationstate);
	}
}
