package com.armadillogamestudios.reclaim.scene.world;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.reclaim.ReclaimGame;
import com.armadillogamestudios.tactics.gameengine.scene.map.World;

public class ReclaimWorld extends World<ReclaimWorldScene, ReclaimGame> {

    public ReclaimWorld(ReclaimWorldScene scene, GameData data) {
        super(scene, data);
    }

}
