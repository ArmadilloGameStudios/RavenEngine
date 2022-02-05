package com.armadillogamestudios.reclaim.scene.world;

import com.armadillogamestudios.engine2d.util.math.Vector3f;
import com.armadillogamestudios.reclaim.ReclaimGame;
import com.armadillogamestudios.reclaim.scene.SagaScene;

public class ReclaimWorldScene extends SagaScene {

    private ReclaimWorld world;

    private Speed speed = Speed.Normal;
    private boolean pausedTick = true;
    private float timeSinceLastTick = 0;

    public boolean moveLeft, moveRight, moveUp, moveDown;

    private final float mapMoveSpeedMultiplier = .1f;

    public ReclaimWorldScene(ReclaimGame game) {
        super(game);

        this.addKeyboardHandler(new MapKeyboardHandler(this));
    }

    @Override
    protected void loadUI() {
        // Background
        setBackgroundColor(new Vector3f(0, 0, 0));

        this.loadWorld();

        this.onPostLoad();
    }

    protected void onPostLoad() {
        world.focus(10442563);
    }

    protected void loadWorld() {
        world = new ReclaimWorld(this, getGame().getReclaimGameData().getReclaimWorldData());
    }

    @Override
    public final void onUpdate(float deltaTime) {
        moveMap(deltaTime);

        if (pausedTick) {
            timeSinceLastTick = 0f;
        } else {
            timeSinceLastTick += deltaTime;

            if (timeSinceLastTick >= speed.getValue()) {
                // TODO multithread tick
                // TODO tick beforehand but don't apply until tick
                tick();
                timeSinceLastTick = 0f;
            }
        }
    }

    private void moveMap(float deltaTime) {

        float speed = deltaTime * mapMoveSpeedMultiplier;

        if (moveUp) {
            getWorldOffset().y -= speed;
        }
        if (moveDown) {
            getWorldOffset().y += speed;
        }
        if (moveRight) {
            getWorldOffset().x -= speed;
        }
        if (moveLeft) {
            getWorldOffset().x += speed;
        }
    }

    public final boolean getPausedTick() {
        return pausedTick;
    }

    public final void setPausedTick(boolean pausedTick) {
        this.pausedTick = pausedTick;

        updatePlayPause();
    }

    protected Speed getSpeed() {
        return speed;
    }

    protected void setSpeed(Speed speed) {
        this.speed = speed;
        this.pausedTick = false;

        updatePlayPause();
    }

    protected void updatePlayPause() {

    }

    protected void updateUI() {

    }

    protected void tick() {

    }

    public enum Speed {
        Slow(500f), Normal(250f), Fast(125f), Fastest(0f);

        private final float value;

        Speed(float value) {
            this.value = value;
        }

        private float getValue() {
            return value;
        }
    }
}
