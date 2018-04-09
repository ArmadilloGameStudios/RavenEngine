package com.raven.breakingsands.scenes.battlescene.decal;

import com.raven.engine2d.util.Factory;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDataQuery;

import java.util.ArrayList;
import java.util.List;

public class DecalFactory extends Factory<Decal> {
    private List<String> types = new ArrayList<>();
    private BattleScene scene;

    public DecalFactory(BattleScene scene) {
        this.scene = scene;
    }

    public Decal getInstance() {
        GameData gameData = Decal.getDataList().queryRandom(new GameDataQuery() {
            @Override
            public boolean matches(GameData row) {

                boolean found = true;

                GameDataList datatypes = row.getList("type");

                for (String type : types) {
                    found &= datatypes.stream().anyMatch(x -> x.asString().equals(type));
                }


                return found;
            }
        });

        return new Decal(scene, gameData);
    }

    public void addTypeRestriction(String type) {
        types.add(type);
    }

    public void clear() {
        types.clear();
    }
}
