package com.raven.engine2d.graphics2d.sprite;

public class SpriteAnimationState {
    private SpriteAnimation animation;
    private SpriteAnimationAction activeAction;
    private SpriteAnimationFrame activeFrame;

    private float time = 0;


    public SpriteAnimationState(SpriteAnimation animation) {
        this.animation = animation;
        this.activeAction = animation.getAction("idle");
        this.activeFrame = activeAction.getFrames().get(0);
    }

    public void update(float deltaTime) {
        time += deltaTime;

        if (time > activeFrame.getTime()) {
            time -= activeFrame.getTime();
            activeFrame = activeAction.getNextFrame(activeFrame, time);
        }
    }

    public int getX() {
        return activeFrame.getX();
    }

    public int getY() {
        return activeFrame.getY();
    }

    public int getWidth() {
        return activeFrame.getWidth();
    }

    public int getHeight() {
        return activeFrame.getHeight();
    }

    public void setAction(String action) {
        this.activeAction = animation.getAction(action);
        this.activeFrame = activeAction.getFrames().get(0);
        this.time = 0;
    }
}
