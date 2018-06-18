package com.raven.breakingsands.scenes.battlescene;

import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.breakingsands.scenes.hud.UIRightContainer;
import com.raven.engine2d.ui.UIButton;

public class UIActionSelect extends UIRightContainer<BattleScene> {

    private UIButton<BattleScene> btnMove, btnAttack, btnSkip, btnCancel, btnLevel, btnEnd;
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

        btnLevel = new UIButton<BattleScene>(scene,
                "sprites/icon level up.png",
                "iconbutton") {

            @Override
            public void handleMouseClick() {
                if (!btnLevel.isDisabled()) {
                    getScene().pawnLevel();
                }
            }
        };
        addChild(btnLevel);

        btnEnd = new UIButton<BattleScene>(scene,
                "sprites/icon end turn.png",
                "iconbutton") {

            @Override
            public void handleMouseClick() {
                if (!btnEnd.isDisabled()) {
                    getScene().pawnEnd();
                }

            }
        };
        addChild(btnEnd);

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
            btnLevel.setDisable(disable);
            btnLevel.setActive(false);
        } else {
            btnCancel.setDisable(!(pawn.getTotalMovement() == pawn.getRemainingMovement()));

            btnAttack.setDisable(false);
            btnMove.setDisable(false);
            btnSkip.setDisable(false);
            btnLevel.setDisable(false);
        }
    }
}
