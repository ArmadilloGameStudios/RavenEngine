package com.raven.engine.worldobject;

public interface Childable<P extends Parentable> {
    void setParent(P parent);
    P getParent();
    void update(float delta);
}
