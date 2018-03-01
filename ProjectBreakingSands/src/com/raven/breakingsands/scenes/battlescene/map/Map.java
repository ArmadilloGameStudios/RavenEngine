package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine.scene.Layer;
import com.raven.engine.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Map extends WorldObject<BattleScene, Layer<WorldObject>, WorldObject> {

    private List<Terrain> terrain = new ArrayList<>();

    public Map(BattleScene scene) {
        super(scene);

        Structure s = new Structure(scene, 0, 0);
        terrain.addAll(s.getTerrainList());
        addChild(s);

        s = new Structure(scene, 0, 3);
        terrain.addAll(s.getTerrainList());
        addChild(s);


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
}
