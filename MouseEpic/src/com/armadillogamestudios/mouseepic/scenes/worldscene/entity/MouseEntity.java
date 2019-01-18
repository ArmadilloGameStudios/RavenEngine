package com.armadillogamestudios.mouseepic.scenes.worldscene.entity;

import com.armadillogamestudios.mouseepic.items.Item;
import com.armadillogamestudios.mouseepic.scenes.worldscene.WorldScene;
import com.armadillogamestudios.mouseepic.scenes.worldscene.terrain.Terrain;
import com.armadillogamestudios.mouseepic.scenes.worldscene.terrain.TerrainTrigger;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.scene.Layer;
import com.raven.engine2d.util.math.Vector2f;
import com.raven.engine2d.util.math.Vector4f;

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

    private float speed = 1.35f;

    private boolean movingUp;
    private boolean movingDown;
    private boolean movingRight;
    private boolean movingLeft;

    private Item item = new Item();

    private Vector4f size = new Vector4f(.2f, .05f, .6f, .5f);
    private Vector2f center = new Vector2f(size.x + size.z / 2f, size.y + size.w / 2f);
    private Vector2f facing = new Vector2f();

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
            facing.x = 0;
            facing.y = 1;
        } else if (movingDown) {
            getAnimationState().setAction("down", false);
            getAnimationState().setFlip(false);
            getAnimationState().setIdleAction("idle_down");
            facing.x = 0;
            facing.y = -1;
        } else if (movingRight) {
            getAnimationState().setAction("side", false);
            getAnimationState().setFlip(false);
            getAnimationState().setIdleAction("idle_side");
            facing.x = 1;
            facing.y = 0;
        } else if (movingLeft) {
            getAnimationState().setAction("side", false);
            getAnimationState().setFlip(true);
            getAnimationState().setIdleAction("idle_side");
            facing.x = -1;
            facing.y = 0;
        } else {
            getAnimationState().setActionIdle();
        }
    }

    public void useItem() {
        Terrain terrain = getScene().getWorldMap().getTerrainAt(getX() + center.x + facing.x, getY() + center.y + facing.y);

        terrain.trigger(item.getEffect().effect);
    }

    @Override
    public Layer.Destination getDestination() {
        return Layer.Destination.Details;
    }

    @Override
    public Vector4f getRect() {
        return size;
    }

    @Override
    public float getZ() {
        return .2f;
    }

    @Override
    public void onUpdate(float deltaTime) {
        updateMovement(deltaTime);
    }

    private void updateMovement(float deltaTime) {
        if (movingUp) {
            if (movingRight) {
                move(speed * deltaTime / 500f,speed *  deltaTime / 500f);
            } else if (movingLeft) {
                move(-speed * deltaTime / 500f, speed * deltaTime / 500f);
            } else {
                move(0, speed * deltaTime / 350f);
            }
        } else if (movingDown) {
            if (movingRight) {
                move(speed * deltaTime / 500f, -speed * deltaTime / 500f);
            } else if (movingLeft) {
                move(-speed * deltaTime / 500f, -speed * deltaTime / 500f);
            } else {
                move(0, -speed * deltaTime / 350f);
            }
        } else {
            if (movingRight) {
                move(speed * deltaTime / 350f, 0);
            } else if (movingLeft) {
                move(-speed * deltaTime / 350f, 0);
            }
        }

        if (movingUp || movingDown || movingRight || movingLeft) {
            getScene().centerView();
        }
    }
}
