package com.armadillogamestudios.mouseepic.scenes.worldscene.entity;

import com.armadillogamestudios.mouseepic.scenes.worldscene.WorldMap;
import com.armadillogamestudios.mouseepic.scenes.worldscene.WorldScene;
import com.armadillogamestudios.mouseepic.scenes.worldscene.terrain.Terrain;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.util.math.Vector2f;
import com.raven.engine2d.util.math.Vector4f;
import com.raven.engine2d.worldobject.WorldObject;

import java.util.Objects;
import java.util.stream.Stream;

public abstract class Entity extends WorldObject<WorldScene, WorldScene, WorldObject> {

    private float slideRate = 2f;

    public Entity(WorldScene scene, GameData data) {
        super(scene, data);
    }

    @Override
    public void move(Vector2f vec) {
        move(vec.x, vec.y);
    }

    @Override
    public void move(float x, float y) {
        WorldMap map = getScene().getWorldMap();

        if (x == 0) {

            Terrain a = map.getTerrainAt(getRect().x + getRect().z + getX(), getY() + getRect().y + y);
            Terrain b = map.getTerrainAt(getRect().x + getX(), getY() + getRect().y + y);
            Terrain c = map.getTerrainAt(getRect().x + getX(), getRect().w + getY() + getRect().y + y);
            Terrain d = map.getTerrainAt(getRect().x + getRect().z + getX(), getRect().w + getY() + getRect().y + y);

            if (y < 0 && a != null && b != null) {
                if (a.isPassable() && !b.isPassable()) {
                    x -= y / slideRate;
                } else if (!a.isPassable() && b.isPassable()) {
                    x += y / slideRate;
                }
            } else if (y > 0 && c != null && d != null) {
                if (c.isPassable() && !d.isPassable()) {
                    x -= y / slideRate;
                } else if (!c.isPassable() && d.isPassable()) {
                    x += y / slideRate;
                }
            }
        }

        Terrain a = map.getTerrainAt(getRect().x + getRect().z + getX() + x, getY() + getRect().y);
        Terrain b = map.getTerrainAt(getRect().x + getX() + x, getY() + getRect().y);
        Terrain c = map.getTerrainAt(getRect().x + getX() + x, getRect().w + getY() + getRect().y);
        Terrain d = map.getTerrainAt(getRect().x + getRect().z + getX() + x, getRect().w + getY() + getRect().y);

        if (Stream.of(a, b, c, d).filter(Objects::nonNull).allMatch(Terrain::isPassable)) {
            moveX(x);
        }

        if (y == 0)
            if (x > 0 && a != null && d != null) {
                if (a.isPassable() && !d.isPassable()) {
                    y -= x / slideRate;
                } else if (!a.isPassable() && d.isPassable()) {
                    y += x / slideRate;
                }
            } else if (x < 0 && b != null && c != null) {
                if (b.isPassable() && !c.isPassable()) {
                    y += x / slideRate;
                } else if (!b.isPassable() && c.isPassable()) {
                    y -= x / slideRate;
                }
            }

        a = map.getTerrainAt(getRect().x + getRect().z + getX(), getY() + getRect().y + y);
        b = map.getTerrainAt(getRect().x + getX(), getY() + getRect().y + y);
        c = map.getTerrainAt(getRect().x + getX(), getRect().w + getY() + getRect().y + y);
        d = map.getTerrainAt(getRect().x + getRect().z + getX(), getRect().w + getY() + getRect().y + y);

        if (Stream.of(a, b, c, d).filter(Objects::nonNull).allMatch(Terrain::isPassable)) {
            moveY(y);
        }
    }

    public abstract Vector4f getRect();
}
