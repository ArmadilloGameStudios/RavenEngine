package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine.scene.Layer;
import com.raven.engine.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Map extends WorldObject<BattleScene, Layer<WorldObject>, WorldObject> {

    private List<Structure> structures = new ArrayList<>();
    private List<Terrain> terrain = new ArrayList<>();

    public Map(BattleScene scene) {
        super(scene);

        StructureFactory structureFactory = new StructureFactory(this);

        Structure s = structureFactory.getInstance();
        structures.add(s);
        terrain.addAll(s.getTerrainList());
        addChild(s);
        System.out.println(s);

        generate(structureFactory);
    }

    int i = 30;
    private boolean generate(StructureFactory structureFactory) {

        List<Structure> structures = this.structures.stream()
                .filter(s ->
                        Arrays.stream(s.getEntrances()).anyMatch(e -> !e.isConnected()))
                .collect(Collectors.toList());

        i--;
        if (structures.size() == 0 || i == 0) {
            return true;
        }

        int randInt = getScene().getRandom().nextInt(structures.size());
        Structure buildFrom = structures.get(randInt);

        StructureEntrance se = Arrays.stream(buildFrom.getEntrances())
                .filter(e -> !e.isConnected()).findAny().get();

        structureFactory.connection(buildFrom, se);

        Structure s = structureFactory.getInstance();

        if (s == null)
            return generate(structureFactory);

        this.structures.add(s);
        terrain.addAll(s.getTerrainList());
        addChild(s);

        return generate(structureFactory);
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
}
