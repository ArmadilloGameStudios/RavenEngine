package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.breakingsands.ZLayer;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.decal.WallFactory;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.scene.Layer;
import com.raven.engine2d.util.pathfinding.Path;
import com.raven.engine2d.util.pathfinding.PathFinder;
import com.raven.engine2d.worldobject.WorldObject;
import org.lwjgl.system.CallbackI;

import java.util.*;
import java.util.stream.Collectors;

public class Map extends WorldObject<BattleScene, Layer<WorldObject>, WorldObject> {

    private List<Structure> structures = new ArrayList<>();
    private List<Terrain> terrain = new ArrayList<>();

    private Structure firstStructure;

    private int size = 2;
    private int i = 0;
    private int tries = 0;

    public Map(BattleScene scene) {
        super(scene);
    }

    public void generate() {
        while (structures.size() == 0 || !structures.contains(firstStructure)) { // TODO already check for size?
            i = size;
            startGeneration();
        }

        // remove islands
        removeIslands();

        addWalls();

        System.out.println(tries + ", " + size + ", " + structures.size());

        // show all connections

    }

    private void startGeneration() {
        System.out.println("Starting Map Gen");

        StructureFactory structureFactory = new StructureFactory(this);

        Structure s = firstStructure = structureFactory.getInstance();
        addStructure(s);

        do {
            i = size - structures.size();
            tries++;
        } while (generate(structureFactory));
    }

    private boolean generate(StructureFactory structureFactory) {
        // find a structure with open connections
        List<Structure> openStructures = this.structures.stream()
                .filter(st -> Arrays.stream(st.getEntrances()).anyMatch(e -> !e.isConnected() && e.anyTerminal(i < 0)))
                .collect(Collectors.toList());

        int sCount = openStructures.size();

        if (sCount == 0) {
//            System.out.println("None Left");
            return false;
        }

        Structure buildFrom = openStructures.get(getScene().getRandom().nextInt(sCount));
        structureFactory.setConnection(buildFrom);
//        System.out.println("BF " + buildFrom.getName());
        structureFactory.setTerminal(i < 0);

        Structure s = structureFactory.getInstance();

        if (s == null) {
            if (buildFrom == firstStructure) return false;
            removeStructure(buildFrom);
        } else {
            addStructure(s);
//            System.out.println("Add " + s.getName());
        }

        return true;
    }

    private void addStructure(Structure s) {
        structures.add(s);
        terrain.addAll(s.getTerrainList());
        addChild(s);
    }

    private void removeStructure(Structure s) {
        removeOnlyStructure(s);
        System.out.println("Remove " + s.getName());

        // remove all not connected to the first
        // get list of connected
        List<Structure> connected = firstStructure.getConnections();

        // remove the ones not there
        List<Structure> toRemove = new ArrayList<>(structures);
        toRemove.removeAll(connected);
        int removedCount = toRemove.size();

        for (Structure r : toRemove) {
            System.out.println("Remove " + r.getName());
            removeOnlyStructure(r);
        }

        redoConnections();
    }

    private void removeOnlyStructure(Structure s) {
        this.structures.remove(s);
        terrain.removeAll(s.getTerrainList());
        removeChild(s);
    }

    private void redoConnections() {
        // redo connections
        for (Structure toRedo : structures) {
            for (StructureEntrance se : toRedo.getEntrances()) {
                se.setConnected(null);
            }
        }

        structures.forEach(st -> structures.forEach(st::tryConnect));
    }

    private void removeIslands() {
        List<Terrain> toRemove = new ArrayList<>();

        Terrain start = terrain.stream().filter(Terrain::isStart).findFirst().get();

        PathFinder<Terrain> terrainPathFinder = new PathFinder<>();

        terrain.forEach(t -> {
            Path<Terrain> path = terrainPathFinder.findTarget(t, start);
            if (path == null) {
                toRemove.add(t);
            }
        });

        terrain.removeAll(toRemove);
        removeChildren(toRemove);
        toRemove.forEach(t -> t.getParent().removeChild(t));
    }

    private void addWalls() {
        WallFactory f = new WallFactory(getScene());
        for (Terrain terrain : terrain) {
            f.clear();

            Optional<Terrain> north = get(terrain.getMapX(), terrain.getMapY() + 1);
            Optional<Terrain> west = get(terrain.getMapX() - 1, terrain.getMapY());
            Optional<Terrain> above = get(terrain.getMapX() - 1, terrain.getMapY() + 1);

            if (!north.isPresent() && !west.isPresent()) {
                f.addTypeRestriction("in");

                terrain.setWall(f.getInstance());
            } else if (!north.isPresent()) {
                f.addTypeRestriction("north");

                if (above.isPresent()) {
                    f.addTypeRestriction("corner");
                } else {
                    f.addTypeRestriction("wall");
                }

                terrain.setWall(f.getInstance());
            } else if (!west.isPresent()) {
                f.addTypeRestriction("west");

                if (above.isPresent()) {
                    f.addTypeRestriction("corner");
                } else {
                    f.addTypeRestriction("wall");
                }

                terrain.setWall(f.getInstance());
            } else if (!above.isPresent()) {
                f.addTypeRestriction("out");

                terrain.setWall(f.getInstance());
            }
        }
    }

    public Optional<Terrain> get(int x, int y) {
        return terrain.stream()
                .filter(t -> t.getMapX() == x && t.getMapY() == y).findFirst();
    }

    public List<Terrain> getTerrainList() {
        return terrain;
    }

    public void setPawn(int x, int y, Pawn p) {
        Optional<Terrain> o = terrain.stream()
                .filter(t -> t.getMapX() == x && t.getMapY() == y).findFirst();

        if (o.isPresent()) {
            Terrain selected = o.get();

            selected.setPawn(p);
        }
    }

    public void setPawn(Terrain t, Pawn p) {
        t.setPawn(p);
    }

    public void setState(Terrain.State state) {
        for (Terrain t : terrain) {
            t.setState(state);
        }
    }

    public List<Structure> getStructures() {
        return structures;
    }

    public Structure getFirstStructure() {
        return firstStructure;
    }

    @Override
    public float getZ() {
        return ZLayer.TERRAIN.getValue();
    }

}
