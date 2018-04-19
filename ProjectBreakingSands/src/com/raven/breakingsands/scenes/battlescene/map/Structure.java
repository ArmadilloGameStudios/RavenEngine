package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.engine2d.GameEngine;
import com.raven.engine2d.database.*;
import com.raven.engine2d.util.math.Vector2i;
import com.raven.engine2d.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Structure extends WorldObject<BattleScene, Map, WorldObject> {

    private int width = 3, height = 3;
    private int x, y;

    private String name;

    private StructureEntrance[] entrances;

    private List<Terrain> terrainList = new ArrayList<>();

    public Structure(BattleScene scene, int x, int y) {
        this(scene,
                GameEngine.getEngine().getGameDatabase().getTable("structure").getRandom(),
                null,
                x, y);
    }

    public Structure(BattleScene scene, GameData gameData, GameData connected, int x, int y) {
        super(scene);

        this.x = x;
        this.y = y;
        this.setX(x);
        this.setY(y);

        width = gameData.getInteger("width");
        height = gameData.getInteger("height");

        name = gameData.getString("name");

        GameDataList gdtList = gameData.getList("terrain");

        TerrainFactory tf = new TerrainFactory(this);

        for (GameData gdt : gdtList) {
            tf.setPropertyData(gdt);

            Terrain t = tf.getInstance();
            addChild(t);
            terrainList.add(t);
        }

        GameDataList gdcList = gameData.getList("entrance");

        entrances = new StructureEntrance[gdcList.size()];

        for (int i = 0; i < gdcList.size(); i++) {
            entrances[i] = new StructureEntrance(this, gdcList.get(i));
        }

    }

    public StructureEntrance[] getEntrances() {
        return entrances;
    }

    public List<Terrain> getTerrainList() {
        return terrainList;
    }

    public String getName() {
        return name;
    }

    public int getMapX() {
        return x;
    }

    public int getMapY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean overlaps(Structure other) {
        if (x >= other.x + other.width ||
                other.x >= x + width) {
            return false;
        }

        return y < other.y + other.height &&
                other.y < y + height;
    }

    public void tryConnect(Structure other) {
        GameDataList connections = GameDatabase.all("connections");

        Arrays.stream(entrances).filter(e -> !e.isConnected()).forEach(e -> Arrays.stream(other.entrances).filter(o -> !o.isConnected()).forEach(o -> {
            if (e.getLength() == o.getLength()) {
                if (connections.stream().anyMatch(con ->
                        con.getList("a").stream().anyMatch(a ->
                                a.getString("name").equals(name) &&
                                        a.getString("entrance").equals(e.getName())) &&
                                con.getList("b").stream().anyMatch(b ->
                                        b.getString("name").equals(other.name) &&
                                                b.getString("entrance").equals(o.getName())) ||
                                con.getList("b").stream().anyMatch(b ->
                                        b.getString("name").equals(name) &&
                                                b.getString("entrance").equals(e.getName())) &&
                                        con.getList("a").stream().anyMatch(a ->
                                                a.getString("name").equals(other.name) &&
                                                        a.getString("entrance").equals(o.getName())))) {


                    boolean connected = false;
                    boolean valid = true;

                    switch (e.getSide()) {
                        case 0:
                            if (o.getSide() != 2) valid = false;
                            break;
                        case 1:
                            if (o.getSide() != 3) valid = false;
                            break;
                        case 2:
                            if (o.getSide() != 0) valid = false;
                            break;
                        case 3:
                            if (o.getSide() != 1) valid = false;
                            break;
                    }

                    if (valid) {

                        Vector2i conPos = StructureEntrance.getEntrancePosition(
                                e.getSide(),
                                e.getLocation(),
                                width,
                                height);

                        conPos.x += x;
                        conPos.y += y;

                        Vector2i gdPos = StructureEntrance.getEntrancePosition(
                                o.getSide(),
                                o.getLocation(),
                                other.width,
                                other.height);

                        gdPos.x += other.x;
                        gdPos.y += other.y;

                        connected = StructureEntrance.isConnected(
                                e.getSide(),
                                e.getLength(),
                                conPos, gdPos);
                    }

                    if (connected) {
                        e.setConnected(o);
                        o.setConnected(e);
                    }
                }
            }
        }));
    }

    public List<Structure> getConnections() {
        List<Structure> connections = new ArrayList<>();
        return getConnections(connections);
    }

    public List<Structure> getConnections(List<Structure> connections) {

        connections.add(this);

        for (StructureEntrance entrance : entrances) {
            if (entrance.getConnection() != null) {
                Structure s = entrance.getConnection().getStructure();

                if (s.getParent().getStructures().contains(s) &&
                        !connections.contains(s)) {
                    s.getConnections(connections);
                }
            }
        }

        return connections;
    }

    @Override
    public float getZ() {
        return .5f;
    }
}
