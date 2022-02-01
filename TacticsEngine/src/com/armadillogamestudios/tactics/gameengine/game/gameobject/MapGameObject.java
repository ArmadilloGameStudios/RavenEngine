package com.armadillogamestudios.tactics.gameengine.game.gameobject;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.graphics2d.DrawStyle;
import com.armadillogamestudios.engine2d.graphics2d.shader.IDMapShader;
import com.armadillogamestudios.engine2d.graphics2d.shader.LayerShader;
import com.armadillogamestudios.engine2d.graphics2d.shader.rendertarget.IDMapDrawObject;
import com.armadillogamestudios.engine2d.graphics2d.sprite.SpriteSheet;
import com.armadillogamestudios.engine2d.input.MouseClickHandler;
import com.armadillogamestudios.engine2d.scene.Layer;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.engine2d.util.math.Vector2f;
import com.armadillogamestudios.engine2d.worldobject.GameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class MapGameObject<S extends Scene<?>, C extends MapChild> extends GameObject<GameObject<?>> implements IDMapDrawObject, MouseClickHandler {

    private Vector2f worldPosition = new Vector2f();

    private final S scene;

    private SpriteSheet mapSpriteSheet, idSpriteSheet;
    private final List<GameObject<?>> parentList = new ArrayList<>();
    private final List<GameObject<?>> children = new CopyOnWriteArrayList<>();
    private final Map<Integer, C> childrenIdMap = new HashMap<>();

    public MapGameObject(S scene, GameData data) {
        this.scene = scene;

        scene.addGameObject(this);
        scene.addIDMap(this);

        this.addMouseHandler(this);

        GameData mapData = data.getData("map");
        String mapSpriteName = mapData.getString("sprite");
        mapSpriteSheet = scene.getEngine().getSpriteSheet(mapSpriteName);
        mapSpriteSheet.load(scene);

        String regionSpriteName = mapData.getString("regionsprite");
        idSpriteSheet = scene.getEngine().getSpriteSheet(regionSpriteName);
        idSpriteSheet.load(scene);

        createChildren(mapData);
    }

    private void createChildren(GameData data) {
        String childName = data.getString("childrenname");

        data.getList(childName).forEach(d -> {
            C child = this.constructChild(d);

            childrenIdMap.put(child.getID(), child);
        });
    }

    @Override
    public Layer.Destination getDestination() {
        return Layer.Destination.Terrain;
    }

    @Override
    public float getZ() {
        return 0.1f;
    }

    @Override
    public void needsRedraw() {

    }

    @Override
    public void draw(LayerShader shader) {
        shader.draw(mapSpriteSheet, null, worldPosition, scene.getWorldOffset(), null, getID(), getZ(), getFade(), getHighlight(), DrawStyle.STANDARD);
    }

    @Override
    public void draw(IDMapShader shader) {
        shader.draw(idSpriteSheet, worldPosition, scene.getWorldOffset(), null, DrawStyle.STANDARD);
    }

    @Override
    public List<GameObject<?>> getParentGameObjectList() {
        return parentList;
    }

    @Override
    public GameObject<?> getParent() {
        return null;
    }

    @Override
    public void setParent(GameObject<?> parent) {

    }

    public S getScene() {
        return scene;
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void addChild(GameObject<?> obj) {

    }

    @Override
    public List<GameObject<?>> getChildren() {
        return children;
    }

    @Override
    public final void handleMouseClick() {
        if (scene.getEngine().getSecondID() != 0) {
            System.out.println(scene.getEngine().getSecondID());
            C child = getChildByID(scene.getEngine().getSecondID());

            if (child != null) {
                child.handleMouseClick();
            }
        }
    }

    public C getChildByID(int id) {
        return childrenIdMap.get(id);
    }

    public abstract C constructChild(GameData gameData);
}
