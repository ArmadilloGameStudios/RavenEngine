package com.armadillogamestudios.storyteller.generator;

import com.armadillogamestudios.storyteller.resource.Resource;

import java.security.acl.Owner;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class Generator<O extends Resource> {
    private O owner;

    public Generator(O owner) {
        this.owner = owner;
    }

    public abstract void generate();

    protected final void spawn(int count, Consumer<O> consumer) {
        for (int i = 0; i < count; i++) {
            consumer.accept(owner);
        }
    }
}