package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.breakingsands.ZLayer;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.scene.Layer;
import com.raven.engine2d.worldobject.WorldObject;
import org.lwjgl.system.CallbackI;

import java.util.*;
import java.util.stream.Collectors;

public class Map extends WorldObject<BattleScene, Layer<WorldObject>, WorldObject> {

    private List<Structure> structures = new ArrayList<>();
    private List<Terrain> terrain = new ArrayList<>();

    private Structure firstStructure;

    private int size = 9;
    private int i = 0;
    private int tries = 0;

    public Map(BattleScene scene) {
        super(scene);

        while (structures.size() == 0 || !structures.contains(firstStructure)) {
            i = size;
            startGeneration();
        }

        System.out.println(tries + ", " + size);

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
                .filter(st -> Arrays.stream(st.getEntrances()).anyMatch(e -> !e.isConnected()))
                .collect(Collectors.toList());

        int sCount = openStructures.size();

        if (sCount == 0) {
            return false;
        }

        Structure buildFrom = openStructures.get(getScene().getRandom().nextInt(sCount));
        structureFactory.setConnection(buildFrom);
        structureFactory.setTerminal(i < 0);

        Structure s = structureFactory.getInstance();

        if (s == null) {
            removeStructure(buildFrom);
        } else {
            addStructure(s);
        }

        // redo all connections
        // TODO remove
//        redoConnections();

        return true;
    }

    private void addStructure(Structure s) {
        structures.add(s);
        terrain.addAll(s.getTerrainList());
        addChild(s);
    }

    private void removeStructure(Structure s) {
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
