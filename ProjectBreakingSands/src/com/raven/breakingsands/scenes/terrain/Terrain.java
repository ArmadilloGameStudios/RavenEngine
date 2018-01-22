package com.raven.breakingsands.scenes.terrain;

import com.raven.breakingsands.scenes.TestScene;
import com.raven.breakingsands.scenes.decal.Decal;
import com.raven.breakingsands.scenes.pawn.Pawn;
import com.raven.engine.GameEngine;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataList;
import com.raven.engine.database.GameDataQuery;
import com.raven.engine.database.GameDatabase;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.util.pathfinding.PathAdjacentNode;
import com.raven.engine.util.pathfinding.PathNode;
import com.raven.engine.worldobject.MouseHandler;
import com.raven.engine.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.List;

public class Terrain extends WorldObject<TestScene, WorldObject> implements MouseHandler, PathNode<Terrain> {
    private static GameDataList dataList = GameDatabase.all("terrain");

    private int x, y;

    private boolean passable = true;

    private Decal decal;
    private Pawn pawn;

    public Terrain(TestScene scene, String name, int x, int y) {
        super(scene, dataList.queryRandom(new GameDataQuery() {
            @Override
            public boolean matches(GameData row) {
                return row.getString("name").matches(name);
            }
        }).getString("model"));

        this.x = x;
        this.y = y;

        this.addMouseHandler(this);
    }

    @Override
    public void handleMouseClick() {
        String desc = "Sand";

        if (decal != null) {
            desc = decal.getDescription();
        }

        desc += "\n";
        desc += "Passable: " + passable;

        if (pawn != null) {
            desc += "\n" + pawn.getName();
        }

        System.out.println(desc);

        if (pawn != null) {
            setHighlight(TestScene.GREEN);
        } else {
            if (passable) {
                setHighlight(TestScene.BLUE);

                Pawn p = getScene().getActivePawn();

                if (this != p.getParent()) {
                    this.setPawn(p);
                    setHighlight(TestScene.GREEN);
                }
            }
            else
                setHighlight(TestScene.YELLOW);
        }
    }

    @Override
    public void handleMouseEnter() {
        if (pawn != null) {
            setHighlight(TestScene.GREEN);
        } else {
            if (passable) {
                setHighlight(TestScene.BLUE);
            }
            else
                setHighlight(TestScene.YELLOW);
        }

    }

    @Override
    public void handleMouseLeave() {
        setHighlight(TestScene.OFF);
    }

    @Override
    public void handleMouseHover(float delta) {

    }

    @Override
    public List<PathAdjacentNode<Terrain>> getAdjacentNodes() {
        List<PathAdjacentNode<Terrain>> neighbors = new ArrayList<>();

        TestScene scene = this.getScene();
        Terrain[][] map = scene.getTerrainMap();
        int size = scene.getTerrainMapSize();


        for (int i = -1; i <=1; i += 2) {
            if (x + i > 0 && x + i < size) {
                Terrain n = map[x + i][y];

                if (n.passable)
                    neighbors.add(new PathAdjacentNode<>(map[x + i][y], 1));
            }
        }

        for (int j = -1; j <= 1; j += 2) {
            if (y + j > 0 && y + j < size) {
                Terrain n = map[x][y + j];

                if (n.passable)
                    neighbors.add(new PathAdjacentNode<>(map[x][y + j], 1));
            }
        }

        return neighbors;
    }

    public static List<ModelData> getModelData() {
        List<ModelData> data = new ArrayList<>();

        for (GameData gameData : dataList) {
            data.add(GameEngine.getEngine().getModelData(gameData.getString("model")));
        }

        return data;
    }

    public void setDecal(Decal decal) {
        if (this.decal != null) {
            removeChild(this.decal);
        }

        this.decal = decal;

        this.passable = decal.isPassable();

        this.addChild(decal);
    }

    public void setPawn(Pawn pawn) {
        if (this.pawn != null) {
            removeChild(this.pawn);
        }

        if (pawn.getParent() != null)
            pawn.getParent().removePawn(pawn);

        this.pawn = pawn;

        this.addChild(pawn);
    }

    public void removePawn(Pawn pawn) {
        this.pawn = null;

        this.removeChild(pawn);
    }
}
