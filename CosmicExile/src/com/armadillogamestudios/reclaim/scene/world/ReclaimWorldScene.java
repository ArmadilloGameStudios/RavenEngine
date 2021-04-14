package com.armadillogamestudios.reclaim.scene.world;

import com.armadillogamestudios.engine2d.ui.UIFont;
import com.armadillogamestudios.engine2d.ui.UILabel;
import com.armadillogamestudios.engine2d.ui.container.UIContainer;
import com.armadillogamestudios.reclaim.CosmicExileGame;
import com.armadillogamestudios.reclaim.data.CosmicExileActiveGameData;
import com.armadillogamestudios.reclaim.data.Player;
import com.armadillogamestudios.reclaim.data.Region;
import com.armadillogamestudios.reclaim.data.World;
import com.armadillogamestudios.tactics.gameengine.scene.map.MapScene;
import com.armadillogamestudios.tactics.gameengine.scene.map.Tile;
import com.armadillogamestudios.tactics.gameengine.scene.map.TileMap;

public class ReclaimWorldScene extends MapScene<ReclaimWorldScene, CosmicExileGame, Region> {

    private static final int ticksPerDay = 24;

    private TileMap<ReclaimWorldScene, Region> map;
    private CosmicExileActiveGameData data;

    private UILabel<ReclaimWorldScene> gold, terrain;

    public ReclaimWorldScene(CosmicExileGame game) {
        super(game);

        data = CosmicExileGame.getActiveGameData();
    }

    @Override
    protected void loadUI() {
         map = new TileMap<ReclaimWorldScene, Region>(this, data.getWorld().getRegions());
         super.loadUI();

        UIContainer<ReclaimWorldScene> terrainBuildingDetailsContainer = new UIContainer<>(this, UIContainer.Location.BOTTOM_RIGHT, UIContainer.Layout.VERTICAL);
        addChild(terrainBuildingDetailsContainer);

        terrain = new UILabel<>(this, "dog", 100, 100);
        terrain.load();
        terrainBuildingDetailsContainer.addChild(terrain);

        UIFont font = terrain.getFont();
        font.setSmall(true);

        terrain.setZ(.5f);
        terrainBuildingDetailsContainer.pack();

        gold = new UILabel<>(this, "", 20, 20);
        gold.load();
        addChild(gold);

        font = gold.getFont();
        font.setSmall(true);

        gold.setZ(.5f);

        updateUI();
    }

    private void updateUI() {
        Player active = data.getPlayers().get(0);
        gold.setText(Integer.toString(active.getGold()));
        gold.load();
    }

    private int ticksSinceLastDay = 0;

    @Override
    protected void tick() {
        World world = data.getWorld();

        updateTick();

        ticksSinceLastDay += 1;
        if (ticksSinceLastDay > ticksPerDay) {
            updateNightly();

            ticksSinceLastDay = 0;
        }

        updateUI();
    }

    private void updateTick() {
        // Movement, Fights, Spawn, etc
    }

    private void updateNightly() {
        // Economy
        data.getPlayers().forEach(player -> {
            player.addGold(10);
        });
    }

    @Override
    protected synchronized TileMap<ReclaimWorldScene, Region> getTileMap() {
        return map;
    }

    @Override
    public void handleTileClick(Region tile) {
        terrain.setText(tile.getSprite());
        terrain.load();
    }
}
