package com.raven.engine2d.graphics2d.shader;

public abstract class ShaderTexture {

    public abstract void load();

    public abstract int getTextureActiveLocation();

    public abstract void release();

    public abstract int getWidth();
    public abstract int getHeight();
}
