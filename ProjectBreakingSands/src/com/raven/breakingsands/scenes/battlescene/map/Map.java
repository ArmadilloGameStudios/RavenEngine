package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.breakingsands.ZLayer;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.scene.Layer;
import com.raven.engine2d.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Map extends WorldObject<BattleScene, Layer<WorldObject>, WorldObject> {

    private List<Structure> structures = new ArrayList<>();
    private List<Terrain> terrain = new ArrayList<>();

    private Structure firstStructure;

    public Map(BattleScene scene) {
        super(scene);

        while (structures.size() == 0) {
            i = 2;
            startGeneration();
        }
    }

    private void startGeneration() {
        StructureFactory structureFactory = new StructureFactory(this);

        Structure s = firstStructure = structureFactory.getInstance();
        structures.add(s);
        terrain.addAll(s.getTerrainList());
        addChild(s);
        System.out.println(s);


        while (generate(structureFactory)) {
        }
    }

    int i = 100;

    private boolean generate(StructureFactory structureFactory) {

        System.out.println(i);

        List<Structure> structures = this.structures.stream()
                .filter(s ->
                        Arrays.stream(s.getEntrances()).anyMatch(e -> !e.isConnected()))
                .collect(Collectors.toList());

        i--;
        if (structures.size() == 0) {
            System.out.println("None left");
            return false;
        }

        int randInt = getScene().getRandom().nextInt(structures.size());
        Structure buildFrom = structures.get(randInt);

        StructureEntrance se = Arrays.stream(buildFrom.getEntrances())
                .filter(e -> !e.isConnected()).findAny().get();

        structureFactory.setClosed(i <= 0);

        structureFactory.setConnection(buildFrom, se);

        Structure s = structureFactory.getInstance();

        if (s == null) {
            System.out.println("Remove");
            int r = removeStructure(buildFrom);
            System.out.println(r);
            i += r;
        } else {
            System.out.println("Add");
            addStructure(s);
        }

        return true;
    }

    private void addStructure(Structure s) {
        structures.add(s);
        terrain.addAll(s.getTerrainList());
        addChild(s);
    }

    private int removeStructure(Structure s) {
        removeOnlyStructure(s);

        // remove all not connected to the first
        // get list of connected
        List<Structure> connected = firstStructure.getConnections();

        // remove the ones not there
        List<Structure> toRemove = new ArrayList<>(structures);
        toRemove.removeAll(connected);
        int removedCount = toRemove.size();

        for (Structure r : toRemove) {
            removeOnlyStructure(r);
        }

        // redo connections
        for (Structure toRedo : structures) {
            for (StructureEntrance se : toRedo.getEntrances()) {
                se.setConnected(null);
            }
        }

        structures.forEach(st -> structures.forEach(st::tryConnect));

        // return the count of removed structures
        removedCount += 1;

        if (removedCount == structures.size()) {
            removedCount -= 1;
        }

        return removedCount;
    }

    private void removeOnlyStructure(Structure s) {
        this.structures.remove(s);
        terrain.removeAll(s.getTerrainList());
        removeChild(s);
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

    @Override
    public float getZ() {
        return ZLayer.TERRAIN.getValue();
    }
}
