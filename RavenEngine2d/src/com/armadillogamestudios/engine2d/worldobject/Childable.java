package com.armadillogamestudios.engine2d.worldobject;

public interface Childable<P extends Parentable> {
    void setParent(P parent);
    P getParent();
    void update(float delta);
}
