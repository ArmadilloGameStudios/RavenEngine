package com.armadillogamestudios.saga.scene.world;

import com.armadillogamestudios.engine2d.util.math.Vector3f;
import com.armadillogamestudios.saga.SagaGame;
import com.armadillogamestudios.saga.data.SagaGameData;
import com.armadillogamestudios.saga.scene.SagaScene;

public class WorldScene extends SagaScene {

    private final float mapMoveSpeedMultiplier = .1f;
    public boolean moveLeft, moveRight, moveUp, moveDown;
    private WorldObject worldObject;
    private RegionDisplay regionDisplay;
    private Speed speed = Speed.Normal;
    private boolean pausedTick = true;
    private float timeSinceLastTick = 0;

    public WorldScene(SagaGame game) {
        super(game);

        this.addKeyboardHandler(new MapKeyboardHandler(this));
    }

    @Override
    protected void loadUI() {
        // Background
        setBackgroundColor(new Vector3f(0, 0, 0));

        regionDisplay = new RegionDisplay(this);
        this.addChild(regionDisplay);

        this.loadWorld();

        this.onPostLoad();
    }

    protected void onPostLoad() {
        worldObject.focus(10442563);
    }

    protected void loadWorld() {
        worldObject = new WorldObject(this, SagaGameData.getSagaWorldData());
    }

    @Override
    public final void onUpdate(float deltaTime) {
        moveMap(deltaTime);

        if (pausedTick) {
            timeSinceLastTick = 0f;
        } else {
            timeSinceLastTick += deltaTime;

            if (timeSinceLastTick >= speed.getValue()) {
                // TODO multi-thread tick
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

    public void setRegion(RegionObject regionObject) {
        worldObject.focus(regionObject.getCenter());
        regionDisplay.setInfo(regionObject.getData());
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
