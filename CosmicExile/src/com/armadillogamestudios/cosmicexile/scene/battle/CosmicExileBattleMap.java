package com.armadillogamestudios.cosmicexile.scene.battle;

import com.armadillogamestudios.cosmicexile.data.location.Location;
import com.armadillogamestudios.tactics.gameengine.scene.battle.BattleMap;

public class CosmicExileBattleMap extends BattleMap<CosmicExileBattleScene> {

    public CosmicExileBattleMap(CosmicExileBattleScene scene, Location location) {
        super(scene, location.getMapSeed(), location.getMapSetting());
    }
}
