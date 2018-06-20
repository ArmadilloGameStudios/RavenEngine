package com.raven.breakingsands.scenes.battlescene;

import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.breakingsands.scenes.hud.UIRightContainer;
import com.raven.engine2d.ui.UIButton;

public class UIActionSelect extends UIRightContainer<BattleScene> {

    private UIButton<BattleScene> btnGravityPull, btnMove, btnAttack, btnSkip, btnCancel, btnLevel, btnEnd;
    private boolean disable;
    private BattleScene.State oldState;

    public UIActionSelect(BattleScene scene) {
        super(scene);

        btnGravityPull = new UIButton<BattleScene>(scene,
                "sprites/move icon.png",
                "iconbutton") {

            @Override
            public void handleMouseClick() {
                if (!isDisabled()) {
                    getScene().pawnGravityPull();
                }
            }
        };
        btnGravityPull.setVisibility(false);
        addChild(btnGravityPull);

        btnMove = new UIButton<BattleScene>(scene,
                "sprites/move icon.png",
                "iconbutton") {

            @Override
            public void handleMouseClick() {
                if (!isDisabled())
                    if (isActive()) {
                        btnMove.setActive(false);
                        scene.setState(oldState = BattleScene.State.SELECT_DEFAULT);
                    } else {
                        btnMove.setActive(true);
                        btnAttack.setActive(false);
                        scene.setState(oldState = BattleScene.State.SELECT_MOVE);
                    }
            }

            @Override
            public void handleMouseEnter() {
                super.handleMouseEnter();

                if (!isDisabled())
                    if (!isActive()) {
                        oldState = scene.getState();
                        scene.setState(BattleScene.State.SELECT_MOVE);
                    }

            }

            @Override
            public void handleMouseLeave() {
                super.handleMouseLeave();

                if (!isDisabled())
                    if (!isActive())
                        scene.setState(oldState);

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
                        scene.setState(oldState = BattleScene.State.SELECT_DEFAULT);
                    } else {
                        btnMove.setActive(false);
                        btnAttack.setActive(true);
                        scene.setState(oldState = BattleScene.State.SELECT_ATTACK);
                    }
            }

            @Override
            public void handleMouseEnter() {
                super.handleMouseEnter();

                if (!isDisabled())
                    if (!isActive()) {
                        oldState = scene.getState();
                        scene.setState(BattleScene.State.SELECT_ATTACK);
                    }

            }

            @Override
            public void handleMouseLeave() {
                super.handleMouseLeave();

                if (!isDisabled())
                    if (!isActive())
                        scene.setState(oldState);

            }
        };
        addChild(btnAttack);

        btnSkip = new UIButton<BattleScene>(scene,
                "sprites/icon skip.png",
                "iconbutton") {

            @Override
            public void handleMouseClick() {
                if (!isDisabled()) {
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
                if (!isDisabled()) {
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
                if (!isDisabled()) {
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
            btnGravityPull.setDisable(disable);
            btnGravityPull.setActive(false);
            btnGravityPull.setVisibility(false);
        } else {
            btnCancel.setDisable(!(pawn.getTotalMovement() == pawn.getRemainingMovement()));

            btnAttack.setDisable(false);
            btnAttack.setActive(false);
            btnMove.setDisable(false);
            btnMove.setActive(false);
            btnSkip.setDisable(false);
            btnSkip.setActive(false);
            btnLevel.setDisable(pawn.getLevel() >= 40); // TODO

            if (pawn.getAbilities().stream().anyMatch(a -> a.gravity_pull)) {
                btnGravityPull.setVisibility(true);
                btnGravityPull.setDisable(false);
            } else {
                btnGravityPull.setVisibility(false);
            }
        }

        btnEnd.setDisable(getScene().getActiveTeam() != 0);

        pack();
    }
}
