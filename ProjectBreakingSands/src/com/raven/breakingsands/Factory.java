package com.raven.breakingsands;

public abstract class Factory<T> {
    public abstract T getInstance();
    public abstract void clear();
}
