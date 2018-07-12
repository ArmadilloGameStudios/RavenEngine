package com.raven.breakingsands.scenes.battlescene;

import com.raven.breakingsands.character.Ability;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.breakingsands.scenes.hud.UIRightContainer;
import com.raven.engine2d.ui.UIButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.raven.breakingsands.scenes.battlescene.BattleScene.State.ATTACKING;
import static com.raven.breakingsands.scenes.battlescene.BattleScene.State.MOVING;
import static com.raven.breakingsands.scenes.battlescene.BattleScene.State.SELECT_DEFAULT;

public class UIActionSelect extends UIRightContainer<BattleScene> {

    private UIAbilityButton btnPushBlast, btnHookPull, btnHack;
    private UIButton<BattleScene> btnMove, btnAttack, btnSkip, btnCancel, btnLevel, btnEnd;
    private List<UIButton<BattleScene>> btns = new ArrayList<>();
    private boolean disable;
    private BattleScene.State oldState = SELECT_DEFAULT;
    private Ability oldAbility;
    private Pawn pawn;

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
                    if (!isActive()) {
                        ;
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
                    if (!isActive()) {
                        scene.setActiveAbility(oldAbility);
                        scene.setState(oldState);
                    }
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
                        scene.setState(oldState = SELECT_DEFAULT);
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
                    if (!isActive()) {
                        ;
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
                    if (!isActive()) {
                        scene.setActiveAbility(oldAbility);
                        scene.setState(oldState);
                    }

            }
        };
        btnHookPull.setVisibility(false);
        addChild(btnHookPull);
        btns.add(btnHookPull);

        btnHack = new UIAbilityButton(scene,
                "sprites/icon hack.png",
                "iconbutton") {

            @Override
            public void handleMouseClick() {
                if (!isDisabled()) {
                    if (isActive()) {
                        setActive(false);
                        getScene().setActiveAbility(null);
                        scene.setState(oldState = SELECT_DEFAULT);
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
                    if (!isActive()) {
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
                    if (!isActive()) {
                        scene.setActiveAbility(oldAbility);
                        scene.setState(oldState);
                    }

            }
        };
        btnHack.setVisibility(false);
        addChild(btnHack);
        btns.add(btnHack);

        btnMove = new UIButton<BattleScene>(scene,
                "sprites/move icon.png",
                "iconbutton") {

            @Override
            public void handleMouseClick() {
                if (!isDisabled())
                    if (isActive()) {
                        setActive(false);
                        scene.setState(oldState = SELECT_DEFAULT);
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
                        scene.setState(oldState = SELECT_DEFAULT);
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
            btnLevel.setVisibility(false);
            btnPushBlast.setDisable(disable);
            btnPushBlast.setActive(false);
            btnPushBlast.setVisibility(false);
            btnHookPull.setDisable(disable);
            btnHookPull.setActive(false);
            btnHookPull.setVisibility(false);
            btnHack.setDisable(disable);
            btnHack.setActive(false);
            btnHack.setVisibility(false);
        } else /*if (pawn != this.pawn)*/ {
            btnCancel.setDisable(!(pawn.getTotalMovement() == pawn.getRemainingMovement()));

            btnAttack.setDisable(!pawn.canAttack());
            btnAttack.setActive(btnAttack.getActive() && pawn == this.pawn);
            btnMove.setDisable(!pawn.canMove());
            btnMove.setActive(btnMove.getActive() && pawn == this.pawn);
            btnSkip.setDisable(false);
//            btnSkip.setActive(false);
            btnCancel.setDisable(false);
//            btnCancel.setActive(false);

            if (pawn.canLevel()) {
                btnLevel.setDisable(false);
                btnLevel.setVisibility(true);
            } else {
                btnLevel.setDisable(true);
                btnLevel.setVisibility(false);
            }

            Optional<Ability> ability = pawn.getAbilities().stream().filter(a -> a.push_blast).findFirst();
            if (ability.isPresent()) {
                btnPushBlast.setVisibility(true);
                btnPushBlast.setDisable(!pawn.canAbility(ability.get()));
                btnPushBlast.setActive(btnPushBlast.getActive() && pawn == this.pawn);
                btnPushBlast.setAbility(ability.get());
            } else {
                btnPushBlast.setVisibility(false);
            }

            ability = pawn.getAbilities().stream().filter(a -> a.hook_pull).findFirst();
            if (ability.isPresent()) {
                btnHookPull.setVisibility(true);
                btnHookPull.setDisable(!pawn.canAbility(ability.get()));
                btnHookPull.setActive(btnHookPull.getActive() && pawn == this.pawn);
                btnHookPull.setAbility(ability.get());
            } else {
                btnHookPull.setVisibility(false);
            }

            ability = pawn.getAbilities().stream().filter(a -> a.hack).findFirst();
            if (ability.isPresent()) {
                btnHack.setVisibility(true);
                btnHack.setDisable(!pawn.canAbility(ability.get()));
                btnHack.setActive(btnHack.getActive() && pawn == this.pawn);
                btnHack.setAbility(ability.get());
            } else {
                btnHack.setVisibility(false);
            }
        }

        btnEnd.setDisable(
                getScene().getActiveTeam() != 0 ||
                        getScene().getState() == MOVING ||
                        getScene().getState() == ATTACKING);

        if (pawn != this.pawn)
            oldState = SELECT_DEFAULT;

        pack();

        this.pawn = pawn;
    }
}
