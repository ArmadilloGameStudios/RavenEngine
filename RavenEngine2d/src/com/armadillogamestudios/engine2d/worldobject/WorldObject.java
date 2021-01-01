package com.armadillogamestudios.engine2d.worldobject;

import com.armadillogamestudios.engine2d.GameProperties;
import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.graphics2d.shader.LayerShader;
import com.armadillogamestudios.engine2d.graphics2d.shader.ShaderTexture;
import com.armadillogamestudios.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.engine2d.util.math.Vector2f;

import javax.sound.sampled.Clip;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class WorldObject<
        S extends Scene<?>,
        C extends WorldObject<S, ?>>
        extends GameObject<C> {

    private Map<String, GameData> audioData;
    private final Map<String, Clip> audioMap = new HashMap<>();

    private final List<GameObject<?>> parentList = new ArrayList<>();
    private S scene;

    private final Vector2f position = new Vector2f();
    private boolean standing;

    private final List<C> children = new CopyOnWriteArrayList<>();

    private String spriteSheetName;
    ShaderTexture spriteSheet;
    private String animationName;
    private SpriteAnimationState spriteAnimationState;

    private GameObject<?> parent;

    public WorldObject(S scene, GameData data) {
        this.scene = scene;
        scene.addGameObject(this);

        if (data.has("standing")) {
            standing = true;
        }

        if (data.has("sprite")) {
            spriteSheetName = data.getString("sprite");
            spriteSheet = scene.getEngine().getSpriteSheet(spriteSheetName);

            if (data.has("animation")) {
                animationName = data.getString("animation");
                spriteAnimationState = new SpriteAnimationState(this, scene.getEngine().getAnimation(animationName));

                data.ifHas("animation_idle", i -> spriteAnimationState.setIdleAction(i.asString()));
            }
        }

        if (data.has("audio")) {
            audioData = data.getData("audio").asMap();

            for (String audioKey : audioData.keySet()) {

                audioMap.put(audioKey,
                        scene.getEngine().getAudioClip(audioData.get(audioKey).asString()));
            }
        }
    }

    public WorldObject(S scene) {
        this.scene = scene;
        scene.addGameObject(this);
    }

    public GameData getWorldObjectData() {
        HashMap<String, GameData> map = new HashMap<>();

        if (standing)
            map.put("standing", new GameData(true));
        if (spriteSheetName != null) {
            map.put("sprite", new GameData(spriteSheetName));

            if (animationName != null) {
                map.put("animation", new GameData(animationName));

                if (!spriteAnimationState.getIdleAction().equals("idle")) {
                    map.put("animation_idle", new GameData(spriteAnimationState.getIdleAction()));
                }
            }
        }
        if (audioData != null) {
            map.put("audio", new GameData(audioData));
        }
        return new GameData(map);
    }

    public String getSpriteSheetName() {
        return spriteSheetName;
    }

    public String getAnimationName() {
        return animationName;
    }

    public float getX() {
        return position.x;
    }

    public void setX(float x) {
        this.needsRedraw();
        position.x = x;
    }

    public void moveX(float x) {
        this.needsRedraw();
        setX(getX() + x);
    }

    public float getY() {
        return position.y;
    }

    public void setY(float y) {
        this.needsRedraw();
        position.y = y;
    }

    public void moveY(float y) {
        this.needsRedraw();
        setY(getY() + y);
    }

    public void move(Vector2f amount) {
        setPosition(getPosition().add(amount, getPosition()));
    }

    public void move(float x, float y) {
        setPosition(getPosition().add(x, y, getPosition()));
    }

    public Vector2f getPosition() {
        return position;
    }

    private final Vector2f worldPos = new Vector2f();

    public Vector2f getWorldPosition() {
        if (getParent() instanceof WorldObject) {
            return position.add(((WorldObject<?, ?>) getParent()).getWorldPosition(), worldPos);
        }
        return position;
    }

    public void setPosition(int x, int y) {
        this.needsRedraw();
        this.position.x = x;
        this.position.y = y;
    }

    public void setPosition(Vector2f position) {
        this.needsRedraw();
        this.position.x = position.x;
        this.position.y = position.y;
    }

    public float getWorldZ() {
        if (getParent() instanceof WorldObject<?,?>) {
            return getZ() + (((WorldObject<?,?>) getParent()).getWorldZ());
        }
        return getZ();
    }

    public S getScene() {
        return scene;
    }

    public void playClip(String name) {
        Clip clip = audioMap.get(name);

        if (clip != null) {
            // Doesn't always work,
            // work around is keep audio shorter than the animation,
            // which also doesn't seem to always work

            clip.stop();
            clip.setFramePosition(0);
            clip.setMicrosecondPosition(0);

            // TODO shouldn't need to be done every time
            if (getScene().getEngine().changeSongVolume(GameProperties.getSFXVolume(), clip)) {
                clip.start();
            } else {
                System.out.println("Missing Audio Controls: " + name);
            }
        }
    }

    public SpriteAnimationState getAnimationState() {
        return spriteAnimationState;
    }

    @Override
    public void draw(LayerShader shader) {
        if (spriteSheet != null)
            shader.draw(spriteSheet, spriteAnimationState, getWorldPosition(), getScene().getWorldOffset(), null, getID(), getWorldZ(), getFade(), getHighlight(), getScene().getDrawStyle());
    }

    public void setParent(GameObject<?> parent) {
        this.parent = parent;
    }

    public GameObject<?> getParent() {
        return parent;
    }

    @Override
    public List<GameObject<?>> getParentGameObjectList() {
        parentList.clear();

        parentList.addAll(parent.getParentGameObjectList());
        parentList.add(parent);

        return parentList;
    }

    @Override
    public List<C> getChildren() {
        return children;
    }

    @Override
    public void addChild(C child) {

        child.setParent(this);
        child.setScene(getScene());

        children.add(child);

        scene.addGameObject(child);
    }

    public void removeAllChildren() {
        children.forEach(c -> {
            scene.removeGameObject(c);
        });
        children.clear();
    }

    public void removeChild(C child) {
        children.remove(child);

        scene.removeGameObject(child);
    }

    public void removeChildren(Collection<? extends C> children) {
//        this.children.removeAll(children);

        children.forEach(this::removeChild);
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

    public void setScene(S scene) {
        this.scene.removeGameObject(this);
        this.scene = scene;
        scene.addGameObject(this);

        children.forEach(c -> c.setScene(scene));
    }

    @Override
    public final void needsRedraw() {
        getScene().getLayer(getDestination()).setNeedRedraw(true);
    }

    protected void setSpriteSheet(String spriteSheetName) {
        this.spriteSheetName = spriteSheetName;
        spriteSheet = scene.getEngine().getSpriteSheet(spriteSheetName);
        spriteSheet.load(scene);
    }

    protected void setAnimation(String animationName) {
        this.animationName = animationName;
        this.spriteAnimationState.setAnimation(getScene().getEngine().getAnimation(animationName));
    }
}
