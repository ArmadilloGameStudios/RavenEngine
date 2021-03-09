package com.armadillogamestudios.cosmicexile.scene.ship.view;

import com.armadillogamestudios.cosmicexile.data.CosmicExileActiveGameData;
import com.armadillogamestudios.cosmicexile.scene.ship.CosmicExileShipScene;
import com.armadillogamestudios.engine2d.ui.container.UIContainer;
import com.armadillogamestudios.tactics.gameengine.game.TacticsGame;
import com.armadillogamestudios.tactics.gameengine.scene.base.BaseScene;
import com.armadillogamestudios.tactics.gameengine.scene.base.BaseView;

public class LocationView extends BaseView<CosmicExileShipScene> {
    @Override
    public void loadUI(UIContainer<CosmicExileShipScene> viewContainer) {
        CosmicExileActiveGameData activeGameData = viewContainer.getScene().getGame().getActiveGameData();
    }
}
