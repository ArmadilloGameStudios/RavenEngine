package com.armadillogamestudios.engine2d.graphics2d.sprite.handler;

public class CountdownActionFinishHandler implements ActionFinishHandler {

    private final ActionFinishHandler handler;
    private int remaining;

    public CountdownActionFinishHandler(ActionFinishHandler handler, int size) {
        this.handler = handler;
        remaining = size;
    }

    @Override
    public void onActionFinish() {
        remaining--;

        if (remaining == 0)
            handler.onActionFinish();
    }
}
