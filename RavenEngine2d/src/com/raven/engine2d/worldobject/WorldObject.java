package com.raven.engine2d.worldobject;

import com.raven.engine2d.database.GameData;
import com.raven.engine2d.graphics2d.sprite.SpriteAnimation;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.util.math.Vector2i;

import java.util.ArrayList;
import java.util.List;

public abstract class WorldObject<
        S extends Scene,
        P extends com.raven.engine2d.worldobject.Parentable<WorldObject>,
        C extends WorldObject>
        extends com.raven.engine2d.worldobject.GameObject<WorldObject, P, C> {

    private List<WorldObject> parentList = new ArrayList<>();
    private S scene;

    private float scale = 1f, rotation = 0f;

    private Vector2i position = new Vector2i();

    private com.raven.engine2d.worldobject.Highlight highlight = new com.raven.engine2d.worldobject.Highlight();

    private List<C> children = new ArrayList<>();

    private SpriteSheet spriteSheet;
    private SpriteAnimation spriteAnimation;

    private long timeOffset = 0;

    P parent;
    boolean parentIsWorldObject;

    public WorldObject(S scene, GameData data) {
        // model
        this.scene = scene;

        if (data.has("sprite")) {
            spriteSheet = com.raven.engine2d.GameEngine.getEngine().getSpriteSheet(data.getString("sprite"));

            if (data.has("animation")) {
                spriteAnimation = com.raven.engine2d.GameEngine.getEngine().getAnimation(data.getString("animation"));
            }
        }
    }


    public int getX() {
        return position.x;
    }

    public void setX(int x) {
        position.x = x;
    }

    public void moveX(int x) {
        setX(getX() + x);
    }

    public int getY() {
        return position.y;
    }

    public void setY(int y) {
        position.y = y;
    }

    public void moveY(int y) {
        setY(getY() + y);
    }

    public void move(Vector2i amount) {
        setPosition(getPosition().add(amount, getPosition()));
    }

    public Vector2i getPosition() {
        return position;
    }

    private Vector2i worldPos = new Vector2i();
    public Vector2i getWorldPosition() {
        if (this.parentIsWorldObject) {
            return position.add(((WorldObject)getParent()).getWorldPosition(), worldPos);
        }
        return position;
    }

    public void setPosition(int x, int y) {
        this.position.x = x;
        this.position.y = y;
    }

    public void setPosition(Vector2i position) {
        this.position.x = position.x;
        this.position.y = position.y;
    }

    public S getScene() {
        return scene;
    }

    public void setHighlight(com.raven.engine2d.worldobject.Highlight h) {
        highlight = h;

        for (C child : children) {
            child.setHighlight(h);
        }
    }

    public com.raven.engine2d.worldobject.Highlight getHighlight() {
        return highlight;
    }

    public void draw() {
        spriteSheet.draw(position, spriteAnimation);

        for (C child : children) {
            child.draw();
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
        if (spriteAnimation != null) {
            spriteAnimation.update(deltaTime);
        }

        this.onUpdate(deltaTime);

        for (C c : children) {
            c.update(deltaTime);
        }
    }

    public void onUpdate(float deltaTime) {

    }
}
