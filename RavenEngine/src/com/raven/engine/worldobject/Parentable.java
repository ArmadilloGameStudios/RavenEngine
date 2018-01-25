package com.raven.engine.worldobject;

import java.util.List;

public interface Parentable<C extends Childable> {
	void addChild(C obj);
	List<C> getChildren();
}
