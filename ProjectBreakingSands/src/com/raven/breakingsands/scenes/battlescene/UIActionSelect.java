package com.raven.breakingsands.scenes.battlescene;

import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
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
                if (!isDisabled())
                    if (isActive()) {
                        btnMove.setActive(false);
                        scene.setState(BattleScene.State.SELECT_DEFAULT);
                    } else {
                        btnMove.setActive(true);
                        btnAttack.setActive(false);
                        scene.setState(BattleScene.State.SELECT_MOVE);
                    }
            }
        };
        addChild(btnMove);

        btnAttack = new UIButton<BattleScene>(scene,
                "sprites/attack icon.png",
                "iconbutton") {

            @Override
            public void handleMouseClick() {
                if (!isDisabled())
                    if (isActive()) {
                        btnAttack.setActive(false);
                        scene.setState(BattleScene.State.SELECT_DEFAULT);
                    } else {
                        btnMove.setActive(false);
                        btnAttack.setActive(true);
                        scene.setState(BattleScene.State.SELECT_ATTACK);
                    }
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

    public void setPawn(Pawn pawn) {
        if (pawn == null) {
            this.disable = true;

            btnAttack.setDisable(disable);
            btnAttack.setActive(false);
            btnCancel.setDisable(disable);
            btnCancel.setActive(false);
            btnMove.setDisable(disable);
            btnMove.setActive(false);
            btnSkip.setDisable(disable);
            btnSkip.setActive(false);
        } else {
            btnCancel.setDisable(!(pawn.getTotalMovement() == pawn.getRemainingMovement()));

            btnAttack.setDisable(false);
            btnMove.setDisable(false);
            btnSkip.setDisable(false);
        }
    }
}
