package com.armadillogamestudios.tactics.gameengine.scene.map;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.util.math.Vector3f;
import com.armadillogamestudios.tactics.gameengine.game.TacticsGame;
import com.armadillogamestudios.tactics.gameengine.scene.TacticsScene;


public abstract class MapScene<S extends MapScene<S, G>, G extends TacticsGame<G>> extends TacticsScene<G> {

    private Speed speed = Speed.Normal;
    private boolean pausedTick = true;
    private float timeSinceLastTick = 0;

    public boolean moveLeft, moveRight, moveUp, moveDown;

    private World<S, G> world;
    private final float mapMoveSpeedMultiplier = .1f;

    public MapScene(final G game) {
        super(game);

        this.addKeyboardHandler(new MapKeyboardHandler(this));
    }

    @Override
    protected void loadUI() {
        // Background
        setBackgroundColor(new Vector3f(0, 0, 0));

        this.setWorld(this.loadWorld());

        this.onPostLoad();
    }

    protected abstract void onPostLoad();

    protected abstract World<S,G> loadWorld();

    private void setWorld(World<S, G> world) {
        this.world = world;
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

    protected abstract void updatePlayPause();

    protected abstract void updateUI();

    protected abstract void tick();

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