package com.armadillogamestudios.cosmicexile.scene.ship.view;

import com.armadillogamestudios.cosmicexile.data.CosmicExileActiveGameData;
import com.armadillogamestudios.cosmicexile.scene.battle.CosmicExileBattleScene;
import com.armadillogamestudios.cosmicexile.scene.ship.CosmicExileShipScene;
import com.armadillogamestudios.engine2d.input.MouseHandler;
import com.armadillogamestudios.engine2d.ui.UILabel;
import com.armadillogamestudios.engine2d.ui.container.UIContainer;
import com.armadillogamestudios.tactics.gameengine.scene.base.BaseView;

public class ContractView extends BaseView<CosmicExileShipScene> {
    @Override
    public void loadUI(UIContainer<CosmicExileShipScene> viewContainer) {
        CosmicExileActiveGameData activeGameData = viewContainer.getScene().getGame().getActiveGameData();

        activeGameData.getContracts().forEach(c -> {
            UILabel<CosmicExileShipScene> uiLabelContract = new UILabel<>(
                    viewContainer.getScene(),
                    c.getName(),
                    100, 100);

            uiLabelContract.addMouseHandler(new MouseHandler() {
                @Override
                public void handleMouseClick() {
                    viewContainer.getScene().getGame().prepTransitionScene(
                            new CosmicExileBattleScene(viewContainer.getScene().getGame(), c));
                }

                @Override
                public void handleMouseEnter() {

                }

                @Override
                public void handleMouseLeave() {

                }

                @Override
                public void handleMouseHover(float delta) {

                }
            });

            viewContainer.addChild(uiLabelContract);
            uiLabelContract.load();
        });
    }

    public CosmicExileShipScene getScene() {
        return null;
    }
}
