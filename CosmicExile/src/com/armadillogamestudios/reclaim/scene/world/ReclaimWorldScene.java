package com.armadillogamestudios.reclaim.scene.world;

import com.armadillogamestudios.reclaim.ReclaimGame;
import com.armadillogamestudios.tactics.gameengine.scene.map.MapScene;
import com.armadillogamestudios.tactics.gameengine.scene.map.World;

public class ReclaimWorldScene extends MapScene<ReclaimWorldScene, ReclaimGame> {

    private ReclaimWorld world;

    public ReclaimWorldScene(ReclaimGame game) {
        super(game);
    }

    @Override
    protected void onPostLoad() {
        world.focus(10442563);
    }

    @Override
    protected World<ReclaimWorldScene, ReclaimGame> loadWorld() {
        world = new ReclaimWorld(this, getGame().getReclaimGameData().getReclaimWorldData());
        return world;
    }

    @Override
    protected void updatePlayPause() {

    }

    @Override
    protected void updateUI() {

    }

    @Override
    protected void tick() {

    }
}
