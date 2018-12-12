package com.armadillogamestudios.mouseepic.scenes.worldscene;

import com.armadillogamestudios.mouseepic.scenes.worldscene.entity.Entity;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.scene.Layer;
import com.raven.engine2d.worldobject.KeyboardHandler;
import com.raven.engine2d.worldobject.MouseHandler;

import java.util.ArrayList;
import java.util.List;

public class MouseEntity extends Entity {
    private static GameData mouseGameData;

    public static List<SpriteSheet> getSpriteSheets(WorldScene scene) {
        List<SpriteSheet> data = new ArrayList<>();

        for (GameData gameData : GameDatabase.all("mouse")) {
            data.add(scene.getEngine().getSpriteSheet(gameData.getString("sprite")));
        }

        return data;
    }

    private boolean movingUp;
    private boolean movingDown;
    private boolean movingRight;
    private boolean movingLeft;

    public static void loadData() {
        for (GameData gameData : GameDatabase.all("mouse")) {
            switch (gameData.getString("name").toLowerCase()) {
                case "mouse":
                    mouseGameData = gameData;
                    break;
            }
        }
    }

    public MouseEntity(WorldScene scene) {
        super(scene, mouseGameData);
    }

    public void setMovingUp(boolean moving) {
        movingUp = moving;
        updateMoving();
    }

    public void setMovingDown(boolean moving) {
        movingDown = moving;
        updateMoving();
    }

    public void setMovingRight(boolean moving) {
        movingRight = moving;
        updateMoving();
    }

    public void setMovingLeft(boolean moving) {
        movingLeft = moving;
        updateMoving();
    }

    private void updateMoving() {
        if (movingUp) {
            getAnimationState().setAction("up", false);
            getAnimationState().setFlip(false);
            getAnimationState().setIdleAction("idle_up");
        } else if (movingDown) {
            getAnimationState().setAction("down", false);
            getAnimationState().setFlip(false);
            getAnimationState().setIdleAction("idle_down");
        } else if (movingRight) {
            getAnimationState().setAction("side", false);
            getAnimationState().setFlip(false);
            getAnimationState().setIdleAction("idle_side");
        } else if (movingLeft) {
            getAnimationState().setAction("side", false);
            getAnimationState().setFlip(true);
            getAnimationState().setIdleAction("idle_side");
        } else {
            getAnimationState().setActionIdle();
        }
    }

    @Override
    public Layer.Destination getDestination() {
        return Layer.Destination.Details;
    }

    @Override
    public float getZ() {
        return .2f;
    }

    @Override
    public void onUpdate(float deltaTime) {
        if (movingUp) {
            if (movingRight) {
                moveX(deltaTime / 500f);
                moveY(deltaTime / 500f);
            } else if (movingLeft) {
                moveX(-deltaTime / 500f);
                moveY(deltaTime / 500f);
            } else {
                moveY(deltaTime / 350f);
            }
        } else if (movingDown) {
            if (movingRight) {
                moveX(deltaTime / 500f);
                moveY(-deltaTime / 500f);
            } else if (movingLeft) {
                moveX(-deltaTime / 500f);
                moveY(-deltaTime / 500f);
            } else {
                moveY(-deltaTime / 350f);
            }
        } else {
            if (movingRight) {
                moveX(deltaTime / 350f);
            } else if (movingLeft) {
                moveX(-deltaTime / 350f);
            }
        }
    }
}
