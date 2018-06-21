package com.raven.breakingsands.scenes.battlescene;

import com.raven.breakingsands.character.Ability;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.breakingsands.scenes.hud.UIRightContainer;
import com.raven.engine2d.ui.UIButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UIActionSelect extends UIRightContainer<BattleScene> {

    private UIAbilityButton btnPushBlast, btnHookPull;
    private UIButton<BattleScene> btnMove, btnAttack, btnSkip, btnCancel, btnLevel, btnEnd;
    private List<UIButton<BattleScene>> btns = new ArrayList<>();
    private boolean disable;
    private BattleScene.State oldState;
    private Ability oldAbility;

    public UIActionSelect(BattleScene scene) {
        super(scene);

        btnPushBlast = new UIAbilityButton(scene,
                "sprites/push icon.png",
                "iconbutton") {

            @Override
            public void handleMouseClick() {
                if (!isDisabled()) {
                    getScene().pawnPushBlast(getAbility());
                }
            }

            @Override
            public void handleMouseEnter() {
                super.handleMouseEnter();

                if (!isDisabled())
                    if (!isActive()) {;
                        oldAbility = scene.getActiveAbility();
                        scene.setActiveAbility(getAbility());
                        oldState = scene.getState();
                        scene.setState(BattleScene.State.SELECT_ABILITY);
                    }

            }

            @Override
            public void handleMouseLeave() {
                super.handleMouseLeave();

                if (!isDisabled())
                    if (!isActive())
                        scene.setActiveAbility(oldAbility);
                scene.setState(oldState);

            }
        };
        btnPushBlast.setVisibility(false);
        addChild(btnPushBlast);
        btns.add(btnPushBlast);

        btnHookPull = new UIAbilityButton(scene,
                "sprites/icon hook.png",
                "iconbutton") {

            @Override
            public void handleMouseClick() {
                if (!isDisabled()) {
                    if (isActive()) {
                        setActive(false);
                        getScene().setActiveAbility(null);
                        scene.setState(oldState = BattleScene.State.SELECT_DEFAULT);
                    } else {
                        btns.forEach(b -> b.setActive(false));
                        setActive(true);
                        getScene().setActiveAbility(getAbility());
                        scene.setState(oldState = BattleScene.State.SELECT_ABILITY);
                    }

                }
            }

            @Override
            public void handleMouseEnter() {
                super.handleMouseEnter();

                if (!isDisabled())
                    if (!isActive()) {;
                        oldAbility = scene.getActiveAbility();
                        scene.setActiveAbility(getAbility());
                        oldState = scene.getState();
                        scene.setState(BattleScene.State.SELECT_ABILITY);
                    }

            }

            @Override
            public void handleMouseLeave() {
                super.handleMouseLeave();

                if (!isDisabled())
                    if (!isActive())
                        scene.setActiveAbility(oldAbility);
                        scene.setState(oldState);

            }
        };
        btnHookPull.setVisibility(false);
        addChild(btnHookPull);
        btns.add(btnHookPull);

        btnMove = new UIButton<BattleScene>(scene,
                "sprites/move icon.png",
                "iconbutton") {

            @Override
            public void handleMouseClick() {
                if (!isDisabled())
                    if (isActive()) {
                        setActive(false);
                        scene.setState(oldState = BattleScene.State.SELECT_DEFAULT);
                    } else {
                        btns.forEach(b -> b.setActive(false));
                        btnMove.setActive(true);
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
        btns.add(btnMove);

        btnAttack = new UIButton<BattleScene>(scene,
                "sprites/attack icon.png",
                "iconbutton") {

            @Override
            public void handleMouseClick() {
                if (!isDisabled())
                    if (isActive()) {
                        setActive(false);
                        scene.setState(oldState = BattleScene.State.SELECT_DEFAULT);
                    } else {
                        btns.forEach(b -> b.setActive(false));
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
        btns.add(btnAttack);

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
        btns.add(btnSkip);

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
        btns.add(btnCancel);

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
        btns.add(btnLevel);

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
        btns.add(btnEnd);

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
            btnPushBlast.setDisable(disable);
            btnPushBlast.setActive(false);
            btnPushBlast.setVisibility(false);
            btnHookPull.setDisable(disable);
            btnHookPull.setActive(false);
            btnHookPull.setVisibility(false);
        } else {
            btnCancel.setDisable(!(pawn.getTotalMovement() == pawn.getRemainingMovement()));

            btnAttack.setDisable(false);
            btnAttack.setActive(false);
            btnMove.setDisable(false);
            btnMove.setActive(false);
            btnSkip.setDisable(false);
            btnSkip.setActive(false);
            btnLevel.setDisable(pawn.getLevel() >= 40); // TODO

            Optional<Ability> ability = pawn.getAbilities().stream().filter(a -> a.push_blast).findFirst();
            if (ability.isPresent()) {
                btnPushBlast.setVisibility(true);
                btnPushBlast.setDisable(false);
                btnPushBlast.setActive(false);
                btnPushBlast.setAbility(ability.get());
            } else {
                btnPushBlast.setVisibility(false);
            }

            ability = pawn.getAbilities().stream().filter(a -> a.hook_pull).findFirst();
            if (ability.isPresent()) {
                btnHookPull.setVisibility(true);
                btnHookPull.setDisable(false);
                btnHookPull.setActive(false);
                btnHookPull.setAbility(ability.get());
            } else {
                btnHookPull.setVisibility(false);
            }
        }

        btnEnd.setDisable(getScene().getActiveTeam() != 0);

        pack();
    }
}
