package com.raven.engine2d.worldobject;

import com.raven.engine2d.GameEngine;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.graphics2d.DrawStyle;
import com.raven.engine2d.graphics2d.shader.MainShader;
import com.raven.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.util.math.Vector2f;

import java.util.ArrayList;
import java.util.List;

public abstract class WorldObject<
        S extends Scene,
        P extends Parentable<WorldObject>,
        C extends WorldObject>
        extends GameObject<WorldObject, P, C> {

    private List<WorldObject> parentList = new ArrayList<>();
    private S scene;

    private float scale = 1f, rotation = 0f;

    private Vector2f position = new Vector2f();

    private Highlight highlight = new Highlight();

    private List<C> children = new ArrayList<>();

    private SpriteSheet spriteSheet;
    private SpriteAnimationState spriteAnimationState;

    private long timeOffset = 0;

    P parent;
    boolean parentIsWorldObject;

    public WorldObject(S scene, GameData data) {
        this.scene = scene;

        if (data.has("sprite")) {
            spriteSheet = GameEngine.getEngine().getSpriteSheet(data.getString("sprite"));

            if (data.has("animation")) {
                String animationName = data.getString("animation");
                spriteAnimationState = new SpriteAnimationState(GameEngine.getEngine().getAnimation(animationName));
            }
        }
    }

    public WorldObject(S scene) {
        this.scene = scene;
    }


    public float getX() {
        return position.x;
    }

    public void setX(float x) {
        position.x = x;
    }

    public void moveX(float x) {
        setX(getX() + x);
    }

    public float getY() {
        return position.y;
    }

    public void setY(float y) {
        position.y = y;
    }

    public void moveY(float y) {
        setY(getY() + y);
    }

    public void move(Vector2f amount) {
        setPosition(getPosition().add(amount, getPosition()));
    }

    public Vector2f getPosition() {
        return position;
    }

    private Vector2f worldPos = new Vector2f();
    public Vector2f getWorldPosition() {
        if (this.parentIsWorldObject) {
            return position.add(((WorldObject)getParent()).getWorldPosition(), worldPos);
        }
        return position;
    }

    public void setPosition(int x, int y) {
        this.position.x = x;
        this.position.y = y;
    }

    public void setPosition(Vector2f position) {
        this.position.x = position.x;
        this.position.y = position.y;
    }

    public S getScene() {
        return scene;
    }

    public void setHighlight(Highlight h) {
        highlight = h;

        for (C child : children) {
            child.setHighlight(h);
        }
    }

    public Highlight getHighlight() {
        return highlight;
    }

    public void draw(MainShader shader) {
        shader.draw(spriteSheet, spriteAnimationState, position, getScene().getWorldOffset(), getID(), DrawStyle.ISOMETRIC);

        for (C child : children) {
            child.draw(shader);
        }
    }

    public void setParent(P parent) {
        this.parent = parent;
    }

    public P getParent() {
        return parent;
    }

    @Override
    public List<WorldObject> getParentGameObjectList() {
        parentList.clear();

        if (parentIsWorldObject) {
            parentList.addAll(((WorldObject) parent).getParentGameObjectList());
            parentList.add((WorldObject) parent);
        }

        return parentList;
    }

    @Override
    public List<C> getChildren() {
        return children;
    }

    @Override
    public void addChild(C child) {
        child.parentIsWorldObject = true;
        child.setParent(this);

        children.add(child);
    }

    public void removeAllChildren() {
        children.clear();
    }

    public void removeChild(WorldObject child) {
        children.remove(child);
    }

    final public void update(float deltaTime) {
        if (spriteAnimationState != null) {
            spriteAnimationState.update(deltaTime);
        }

        this.onUpdate(deltaTime);

        for (C c : children) {
            c.update(deltaTime);
        }
    }

    public void onUpdate(float deltaTime) {

    }
}
