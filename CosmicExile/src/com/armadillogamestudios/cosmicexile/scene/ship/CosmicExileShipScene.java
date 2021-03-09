package com.armadillogamestudios.cosmicexile.scene.ship;

import com.armadillogamestudios.cosmicexile.game.CosmicExileGame;
import com.armadillogamestudios.cosmicexile.scene.ship.view.ContractView;
import com.armadillogamestudios.cosmicexile.scene.ship.view.ReputationView;
import com.armadillogamestudios.tactics.gameengine.scene.base.BaseScene;
import com.armadillogamestudios.tactics.gameengine.scene.base.BaseView;

import java.util.Arrays;
import java.util.List;

public class CosmicExileShipScene extends BaseScene<CosmicExileShipScene, CosmicExileGame> {

    private final List<BaseView<CosmicExileShipScene>> views = Arrays.asList(new ContractView(), new ReputationView());

    public CosmicExileShipScene(CosmicExileGame game) {
        super(game);
    }

    @Override
    protected List<BaseView<CosmicExileShipScene>> getViews() {
        return views;
    }

}
