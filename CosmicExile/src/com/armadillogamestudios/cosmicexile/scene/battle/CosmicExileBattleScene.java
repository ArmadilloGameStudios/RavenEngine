package com.armadillogamestudios.cosmicexile.scene.battle;

import com.armadillogamestudios.cosmicexile.data.contract.Contract;
import com.armadillogamestudios.cosmicexile.game.CosmicExileGame;
import com.armadillogamestudios.engine2d.database.GameDatabase;
import com.armadillogamestudios.tactics.gameengine.scene.battle.BattleMap;
import com.armadillogamestudios.tactics.gameengine.scene.battle.BattleScene;

public class CosmicExileBattleScene extends BattleScene<CosmicExileGame> {

    private final Contract contract;

    private CosmicExileBattleMap map;

    public CosmicExileBattleScene(CosmicExileGame game, Contract contract) {
        super(game);

        this.contract = contract;
    }

    @Override
    public void updateUI(float deltaTime) {

    }

    @Override
    protected void loadUI() {
        map = new CosmicExileBattleMap(this, contract.getLocation());
    }
}