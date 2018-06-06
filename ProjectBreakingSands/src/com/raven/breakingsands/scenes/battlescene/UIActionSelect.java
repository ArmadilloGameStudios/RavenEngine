package com.raven.breakingsands.scenes.battlescene;

import com.raven.breakingsands.scenes.hud.UIRightContainer;
import com.raven.engine2d.ui.UIButton;

public class UIActionSelect extends UIRightContainer<BattleScene> {

    private UIButton<BattleScene> btnMove, btnAttack, btnSkip, btnCancel;
    private boolean disable;

    public UIActionSelect(BattleScene scene) {
        super(scene);

        btnMove = new UIButton<BattleScene>(scene,
                "sprites/move icon.png",
                "iconbutton") {

            @Override
            public void handleMouseClick() {

            }
        };
        addChild(btnMove);

        btnAttack = new UIButton<BattleScene>(scene,
                "sprites/attack icon.png",
                "iconbutton") {

            @Override
            public void handleMouseClick() {

            }
        };
        addChild(btnAttack);

        btnSkip = new UIButton<BattleScene>(scene,
                "sprites/icon skip.png",
                "iconbutton") {

            @Override
            public void handleMouseClick() {
                if (!btnSkip.isDisabled()) {
                    getScene().pawnWait();
                }
            }
        };
        addChild(btnSkip);

        btnCancel = new UIButton<BattleScene>(scene,
                "sprites/cancel icon.png",
                "iconbutton") {

            @Override
            public void handleMouseClick() {
                if (!btnCancel.isDisabled()) {
                    getScene().pawnDeselect();
                }
            }
        };
        addChild(btnCancel);

        pack();
    }

    public void setDisable(boolean disable) {
        this.disable = disable;

        btnAttack.setDisable(disable);
        btnCancel.setDisable(disable);
        btnMove.setDisable(disable);
        btnSkip.setDisable(disable);
    }
}
