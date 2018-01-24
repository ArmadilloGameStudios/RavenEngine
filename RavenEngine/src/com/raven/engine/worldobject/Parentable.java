package com.raven.engine.worldobject;

public interface Parentable<C extends Childable> {
	void addChild(C obj);
}
