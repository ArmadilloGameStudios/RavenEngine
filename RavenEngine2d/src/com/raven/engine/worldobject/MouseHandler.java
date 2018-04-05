package com.raven.engine.worldobject;

public interface MouseHandler {
	void handleMouseClick();
	void handleMouseEnter();
	void handleMouseLeave();
	void handleMouseHover(float delta);
}
