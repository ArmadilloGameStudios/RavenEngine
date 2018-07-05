package com.raven.engine2d.graphics2d.sprite;

import com.raven.engine2d.GameEngine;
import com.raven.engine2d.graphics2d.sprite.handler.ActionFinishHandler;
import com.raven.engine2d.graphics2d.sprite.handler.FrameFinishHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SpriteAnimationState {
    private SpriteAnimation animation;
    private SpriteAnimationAction activeAction;
    private SpriteAnimationFrame activeFrame;

    private List<FrameFinishHandler> frameFinishHandlers = new CopyOnWriteArrayList<>();
    private List<ActionFinishHandler> actionFinishHandlers = new CopyOnWriteArrayList<>();

    private float time = 0;
    private boolean flip = false;
    private String idleAction = "idle";


    public SpriteAnimationState(SpriteAnimation animation) {
        this.animation = animation;
        this.activeAction = animation.getAction(idleAction);
        this.activeFrame = activeAction.getFrames().get(0);
    }

    public void update(float deltaTime) {
        time += deltaTime;

        if (time > activeFrame.getTime()) {
            for (FrameFinishHandler handler : frameFinishHandlers) {
                handler.onFrameFinish(this);
            }
            frameFinishHandlers.clear();

            time -= activeFrame.getTime();
            activeFrame = activeAction.getNextFrame(activeFrame, time);

            if (activeFrame.getIndex() == 0) {
                for (ActionFinishHandler handler : actionFinishHandlers) {
                    handler.onActionFinish(this);
                }
            }
        }
    }

    public int getX() {
        return activeFrame.getX();
    }

    public int getY() {
        return activeFrame.getY();
    }

    public float getXOffset() {
        return activeFrame.getXOffset();
    }

    public float getYOffset() {
        return activeFrame.getYOffset();
    }

    public int getWidth() {
        return activeFrame.getWidth();
    }

    public int getHeight() {
        return activeFrame.getHeight();
    }

    public boolean hasAction(String action) {
        return animation.hasAction(action);
    }

    public void setAction(String action) {
        setAction(action, true);
    }

    public void setAction(String action, boolean reset) {
        actionFinishHandlers.clear();
        if (reset || !activeAction.getName().equals(action)) {
            this.activeAction = animation.getAction(action);
            this.activeFrame = activeAction.getFrames().get(0);
            this.time = 0;
        }
    }

    public void setIdleAction(String idleAction) {
        if (activeAction.getName().equals(this.idleAction)) {
            setAction(idleAction);
        }
        this.idleAction = idleAction;
    }

    public String getIdleAction() {
        return idleAction;
    }

    public void setActionIdle() {
        setAction(idleAction);
    }

    public void setActionIdle(boolean b) {
        setAction(idleAction, b);
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    public boolean getFlip() {
        return flip;
    }

    public void addFrameFinishHandler(FrameFinishHandler handler) {
        frameFinishHandlers.add(handler);
    }

    public void addActionFinishHandler(ActionFinishHandler handler) {
        actionFinishHandlers.add(handler);
    }
}
